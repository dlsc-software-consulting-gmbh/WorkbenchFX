package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the master view, which is used to show all view parts.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxView extends StackPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxView.class.getName());
  final ToolbarView toolbarView;
  final HomeView homeView;
  final ContentView contentView;
  VBox viewBox;

  /**
   * Displays all of the view parts, representing the master view.
   */
  public WorkbenchFxView(
      ToolbarView toolbarView,
      HomeView homeView,
      ContentView contentView) {
    this.toolbarView = toolbarView;
    this.homeView = homeView;
    this.contentView = contentView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("workbench");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    viewBox = new VBox();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    viewBox.getChildren().addAll(toolbarView, contentView);
    getChildren().addAll(viewBox);
    VBox.setVgrow(contentView, Priority.ALWAYS);
  }

  /**
   * Stacks the {@code overlay} on top of the current view, together with its {@code glassPane} in
   * the background and makes the {@code glassPane} hide, whenever the overlay is hidden.
   *
   * @param overlay to be stacked on top of the view
   * @param glassPane to be added in the background of the {@code overlay}
   */
  public void addOverlay(Node overlay, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    overlay.setVisible(false);
    getChildren().addAll(glassPane, overlay);
    // make glass pane hide if overlay is not showing
    glassPane.hideProperty().bind(overlay.visibleProperty().not());
  }

  /**
   * Removes the {@code overlay} from the scene graph and removes the bindings created with the call
   * to {@link WorkbenchFxView#addOverlay(Node, GlassPane)}.
   * @param overlay to be removed from the scene graph
   * @param glassPane the {@code overlay}'s corresponding {@link GlassPane}
   */
  public void removeOverlay(Node overlay, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    glassPane.hideProperty().unbind();
    getChildren().removeAll(glassPane, overlay);
  }

  /**
   * Makes the {@code overlay} visible.
   *
   * @param overlay to be made visible
   */
  public void showOverlay(Node overlay) {
    LOGGER.trace("showOverlay");
    overlay.setVisible(true);
  }

  /**
   * Makes the {@code overlay} <b>in</b>visible.
   *
   * @param overlay to be made <b>in</b>visible
   */
  public void hideOverlay(Node overlay) {
    LOGGER.trace("hideOverlay");
    overlay.setVisible(false);
  }

}
