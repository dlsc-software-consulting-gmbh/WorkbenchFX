package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the presenter of the corresponding {@link WorkbenchFxView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxPresenter.class.getName());

  private WorkbenchFx model;
  private WorkbenchFxView view;

  private final ObservableMap<Node, GlassPane> overlays;
  private final ObservableSet<Node> overlaysShown;
  private final ObservableSet<Node> blockingOverlaysShown;

  /**
   * Constructs a new {@link WorkbenchFxPresenter} for the {@link WorkbenchFxView}.
   *
   * @param model the model of WorkbenchFX
   * @param view  corresponding view to this presenter
   */
  public WorkbenchFxPresenter(WorkbenchFx model, WorkbenchFxView view) {
    this.model = model;
    this.view = view;
    overlays = model.getOverlays();
    overlaysShown = model.getOverlaysShown();
    blockingOverlaysShown = model.getBlockingOverlaysShown();
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.contentView.setContent(view.homeView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    // When the active module changes, the new view is set od the home screen if null.
    model.activeModuleViewProperty().addListener((observable, oldModule, newModule) ->
        view.contentView.setContent(Objects.isNull(newModule) ? view.homeView : newModule)
    );

    overlays.addListener((MapChangeListener<Node, GlassPane>) c -> {
      LOGGER.trace("Listener overlays fired");
      if (c.wasAdded()) {
        LOGGER.trace("Overlay added");
        addOverlay(c.getKey(), c.getValueAdded());
      } else if (c.wasRemoved()) {
        LOGGER.trace("Overlay removed");
        removeOverlay(c.getKey(), c.getValueRemoved());
      }
    });

    WorkbenchFxUtils.addSetListener(
        overlaysShown,
        (SetChangeListener.Change<? extends Node> c) -> showOverlay(c.getElementAdded(), false),
        (SetChangeListener.Change<? extends Node> c) -> hideOverlay(c.getElementAdded(), false)
    );

    WorkbenchFxUtils.addSetListener(
        blockingOverlaysShown,
        (SetChangeListener.Change<? extends Node> c) -> showOverlay(c.getElementAdded(), true),
        (SetChangeListener.Change<? extends Node> c) -> hideOverlay(c.getElementAdded(), true)
    );

    // hide or show navigation drawer depending on property in model
    model.navigationDrawerShownProperty().addListener((observable, oldShown, newShown) -> {
      if (oldShown == newShown) {
        return;
      }
      Node navigationDrawer = model.getNavigationDrawer();
      if (newShown) {
        model.showOverlay(navigationDrawer, false);
      } else {
        model.hideOverlay(navigationDrawer, false);
      }
    });
  }

  /**
   * TODO
   *
   * @param overlay
   * @param glassPane
   */
  public void addOverlay(Node overlay, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    view.addOverlay(overlay, glassPane);
  }

  /**
   * TODO
   *
   * @param overlay
   * @param glassPane
   */
  public void removeOverlay(Node overlay, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    view.removeOverlay(overlay, glassPane);

    // invalidate previous event handler, if existent (when blocking)
    glassPane.setOnMouseClicked(null);
  }

  /**
   * TODO
   * @param overlay
   * @param blocking
   */
  public void showOverlay(Node overlay, boolean blocking) {
    showOverlay(overlay, overlays.get(overlay), blocking);
  }

  /**
   * TODO
   * @param overlay
   * @param glassPane
   */
  public void showOverlay(Node overlay, GlassPane glassPane, boolean blocking) {
    LOGGER.trace("showOverlay - Blocking: " + blocking);
    view.showOverlay(overlay);

    // if overlay is not blocking, make the overlay hide when the glass pane is clicked
    if (!blocking) {
      glassPane.setOnMouseClicked(event -> model.hideOverlay(overlay, false));
    }
  }

  /**
   * TODO
   * @param overlay
   * @param blocking
   */
  public void hideOverlay(Node overlay, boolean blocking) {
    hideOverlay(overlay, overlays.get(overlay), blocking);
  }

  /**
   * TODO
   * @param overlay
   * @param glassPane
   */
  public void hideOverlay(Node overlay, GlassPane glassPane, boolean blocking) {
    LOGGER.trace("hideOverlay - Blocking: " + blocking);
    view.hideOverlay(overlay);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }
}
