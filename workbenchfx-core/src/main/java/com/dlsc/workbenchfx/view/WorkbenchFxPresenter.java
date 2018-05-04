package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.overlay.Overlay;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
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

  private ObservableMap<Overlay, GlassPane> overlays;

  /**
   * Constructs a new {@link WorkbenchFxPresenter} for the {@link WorkbenchFxView}.
   *
   * @param model the model of WorkbenchFX
   * @param view  corresponding view to this presenter
   */
  public WorkbenchFxPresenter(WorkbenchFx model, WorkbenchFxView view) {
    this.model = model;
    overlays = model.getOverlays();
    this.view = view;
    init();

    initializeOverlays();
  }

  private void initializeOverlays() {
    // initially load all overlays and hide them
    model.getOverlays().entrySet().stream().forEachOrdered(entry -> {
      addOverlay(entry.getKey(), entry.getValue());
    });
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

    overlays.addListener(
        (MapChangeListener<Overlay, GlassPane>)
            c -> {
              LOGGER.trace("Listener overlays fired");
              if (c.wasAdded()) {
                LOGGER.trace("Overlay added");
                addOverlay(c.getKey(), c.getValueAdded());
              } else if (c.wasRemoved()) {
                LOGGER.trace("Overlay removed");
                removeOverlay(c.getKey(), c.getValueRemoved());
              }
            });
  }

  /**
   * TODO
   * @param overlay
   * @param glassPane
   */
  public void addOverlay(Overlay overlay, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    Node overlayNode = overlay.getNode();
    view.addOverlay(overlayNode, glassPane);

    // if overlay is not blocking, make the overlay hide when the glass pane is clicked
    if (!overlay.isBlocking()) {
      glassPane.setOnMouseClicked(event -> overlayNode.setVisible(false));
    }
  }

  /**
   * TODO
   * @param overlay
   * @param glassPane
   */
  public void removeOverlay(Overlay overlay, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    Node overlayNode = overlay.getNode();
    view.removeOverlay(overlayNode, glassPane);

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
