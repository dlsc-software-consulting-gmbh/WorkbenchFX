package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import java.util.function.Consumer;
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

    WorkbenchFxUtils.addSetListener(
        overlaysShown,
        (SetChangeListener.Change<? extends Node> c) -> addOverlay(c.getElementAdded(), false),
        (SetChangeListener.Change<? extends Node> c) -> removeOverlay(c.getElementAdded(), false)
    );

    WorkbenchFxUtils.addSetListener(
        blockingOverlaysShown,
        (SetChangeListener.Change<? extends Node> c) -> addOverlay(c.getElementAdded(), true),
        (SetChangeListener.Change<? extends Node> c) -> removeOverlay(c.getElementAdded(), true)
    );
  }

  /**
   * TODO
   * @param overlay
   * @param blocking
   */
  public void addOverlay(Node overlay, boolean blocking) {
    addOverlay(overlay, overlays.get(overlay), blocking);
  }

  /**
   * TODO
   * @param overlay
   * @param glassPane
   */
  public void addOverlay(Node overlay, GlassPane glassPane, boolean blocking) {
    LOGGER.trace("addOverlay - Blocking: " + blocking);
    view.addOverlay(overlay, glassPane);

    // if overlay is not blocking, make the overlay hide when the glass pane is clicked
    if (!blocking) {
      glassPane.setOnMouseClicked(event -> {
        // TODO: is animation still being displayed?
        model.hideOverlay(overlay, false);
      });
    }
  }

  /**
   * TODO
   * @param overlay
   * @param blocking
   */
  public void removeOverlay(Node overlay, boolean blocking) {
    removeOverlay(overlay, overlays.get(overlay), blocking);
  }

  /**
   * TODO
   * @param overlay
   * @param glassPane
   */
  public void removeOverlay(Node overlay, GlassPane glassPane, boolean blocking) {
    LOGGER.trace("removeOverlay - Blocking: " + blocking);
    view.removeOverlay(overlay, glassPane);

    // invalidate previous event handler, if existent
    glassPane.setOnMouseClicked(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    // hide or show navigation drawer depending on property in model
    model.getNavigationDrawer().getNode().visibleProperty().bindBidirectional(model.navigationDrawerShownProperty());
  }
}
