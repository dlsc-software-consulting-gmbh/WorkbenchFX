package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchOverlay;
import com.dlsc.workbenchfx.util.WorkbenchUtils;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import java.util.Objects;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the presenter of the corresponding {@link WorkbenchView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class WorkbenchPresenter extends Presenter {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkbenchPresenter.class.getName());

  private final Workbench model;
  private final WorkbenchView view;

  private final ObservableMap<Region, WorkbenchOverlay> overlays;
  private final ObservableList<Region> overlaysShown;
  private final ObservableList<Region> blockingOverlaysShown;

  /**
   * Constructs a new {@link WorkbenchPresenter} for the {@link WorkbenchView}.
   *
   * @param model the model of WorkbenchFX
   * @param view corresponding view to this presenter
   */
  public WorkbenchPresenter(Workbench model, WorkbenchView view) {
    this.model = model;
    this.view = view;
    this.overlays = model.getOverlays();
    this.overlaysShown = model.getNonBlockingOverlaysShown();
    this.blockingOverlaysShown = model.getBlockingOverlaysShown();

    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeViewParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupValueChangedListeners() {
    overlays.addListener((MapChangeListener<Region, WorkbenchOverlay>) c -> {
      LOGGER.trace("Listener overlays fired");
      if (c.wasAdded()) {
        LOGGER.trace("Overlay added");
        addOverlay(c.getValueAdded());
      } else if (c.wasRemoved()) {
        LOGGER.trace("Overlay removed");
        removeOverlay(c.getValueRemoved());
      }
    });

    WorkbenchUtils.addListListener(
        overlaysShown,
        change -> showOverlay(change, false),
        this::hideOverlay
    );

    WorkbenchUtils.addListListener(
        blockingOverlaysShown,
        change -> showOverlay(change, true),
        this::hideOverlay
    );
  }

  /**
   * Adds an {@code overlay} together with the {@code glassPane} to the view.
   *
   * @param overlay to be added
   */
  private void addOverlay(WorkbenchOverlay overlay) {
    LOGGER.trace("addOverlay");
    view.addOverlay(overlay.getOverlay(), overlay.getGlassPane());
  }

  /**
   * Removes an {@code overlay} together with the {@code glassPane} from the view.
   *
   * @param overlay to be removed
   */
  private void removeOverlay(WorkbenchOverlay overlay) {
    LOGGER.trace("removeOverlay");
    view.removeOverlay(overlay.getOverlay(), overlay.getGlassPane());

    // invalidate previous event handler, if existent (when blocking)
    overlay.getGlassPane().setOnMouseClicked(null);
  }

  /**
   * Makes the {@code overlay} visible, along with its {@code glassPane}.
   *
   * @param overlay to be shown
   * @param blocking if false, will make {@code overlay} hide, if its {@code glassPane} was clicked
   */
  private void showOverlay(Region overlay, boolean blocking) {
    showOverlay(model.getOverlays().get(overlay), blocking);
  }

  /**
   * Makes the {@code overlay} visible, along with its {@code glassPane}.
   *
   * @param workbenchOverlay the {@code overlay}'s corresponding model object
   * @param blocking if false, will make {@code overlay} hide, if its {@code glassPane} was clicked
   */
  private void showOverlay(WorkbenchOverlay workbenchOverlay, boolean blocking) {
    LOGGER.trace("showOverlay - Blocking: " + blocking);
    Region overlay = workbenchOverlay.getOverlay();
    if (workbenchOverlay.isAnimated()) {
      if (overlay.getWidth() != 0) {
        workbenchOverlay.getAnimationStart().play();
      }
    }
    view.showOverlay(overlay);

    // if overlay is not blocking, make the overlay hide when the glass pane is clicked
    if (!blocking) {
      LOGGER.trace("showOverlay - Set GlassPane EventHandler");
      workbenchOverlay.getGlassPane().setOnMouseClicked(event -> {
        // check if overlay is really not blocking, is needed to avoid false-positives
        if (overlaysShown.contains(overlay)) {

          if (overlay == model.getDrawerShown()) {
            // if the overlay is the drawer that is currently being shown
            LOGGER.trace("GlassPane was clicked, hiding drawer");
            model.hideDrawer();
          } else if (overlay instanceof DialogControl) {
            // if the overlay is a dialog
            LOGGER.trace("GlassPane was clicked, hiding dialog");
            WorkbenchDialog dialog = ((DialogControl) overlay).getDialog();
            // send cancel button type as result of the dialog if available
            ButtonType cancelButtonType = dialog.getDialogControl().getCancelButtonType();
            // if not available, send the defined cancelDialogButtonType
            if (Objects.isNull(cancelButtonType)) {
              cancelButtonType = ButtonType.CANCEL;
            }
            dialog.getOnResult().accept(cancelButtonType);
            model.hideDialog(dialog);
          } else {
            LOGGER.trace("GlassPane was clicked, hiding overlay");
            model.hideOverlay(overlay);
          }
        }
      });
    }
  }

  /**
   * Makes the {@code overlay} <b>in</b>visible, along with its {@code glassPane}.
   *
   * @param overlay to be hidden
   */
  private void hideOverlay(Region overlay) {
    hideOverlay(model.getOverlays().get(overlay));
  }

  /**
   * Makes the {@code overlay} <b>in</b>visible, along with its {@code glassPane}.
   *
   * @param overlay to be hidden
   */
  private void hideOverlay(WorkbenchOverlay overlay) {
    if (overlay.isAnimated()) {
      overlay.getAnimationEnd().play();
      // make sure GlassPane starts hiding at the same time as the animation, not when the animation
      // is finished and the overlay has been hidden
      overlay.getGlassPane().setHide(true);
    } else {
      view.hideOverlay(overlay.getOverlay());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupBindings() {

  }
}
