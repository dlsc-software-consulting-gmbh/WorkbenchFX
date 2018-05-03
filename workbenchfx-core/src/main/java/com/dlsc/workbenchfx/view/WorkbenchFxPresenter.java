package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.overlay.Overlay;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
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
    model.getOverlays().forEach(this::addOverlay);
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
              LOGGER.trace("Listener getOverlays fired");
              if (c.wasAdded()) {

              }
              while (c.next()) {
                LOGGER.trace(
                    String.format(
                        "Changed - Added: %s, Removed: %s", c.getAddedSize(), c.getRemovedSize()));
                if (c.wasRemoved()) {
                  for (Node node : c.getRemoved()) {
                    LOGGER.trace("Overlay removed");
                    view.getChildren().remove(node);
                  }
                }
                if (c.wasAdded()) {
                  for (Node node : c.getAddedSubList()) {
                    LOGGER.trace("Overlay added");
                    addOverlay(node);
                  }
                }
              }
            });
  }

  private void addOverlay(Overlay overlay, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    Node overlayNode = overlay.init(model);
    overlayNode.setVisible(false);
    view.getChildren().add(overlayNode);
    Bindings.bindBidirectional(glassPane.hideProperty(), overlayNode.visibleProperty().not());
    Bindings.bind
    glassPane.hideProperty().bindBidirectional(overlayNode.visibleProperty());
  }

  private void removeOverlay(Overlay overlay, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    Node overlayNode = overlay.init(model);
    overlayNode.setVisible(false);
    view.getChildren().add(overlayNode);
    glassPane.hideProperty().bind(overlayNode.visibleProperty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    view.glassPane.hideProperty().bind(model.glassPaneShownProperty().not());
  }
}
