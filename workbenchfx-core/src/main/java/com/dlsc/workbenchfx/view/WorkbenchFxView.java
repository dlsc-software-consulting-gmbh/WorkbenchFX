package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.overlay.Overlay;
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

  public void addOverlay(Node overlayNode, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    overlayNode.setVisible(false);
    getChildren().addAll(glassPane, overlayNode);
    // make glass pane hide if overlay is not showing
    glassPane.hideProperty().bind(overlayNode.visibleProperty().not());
  }

  public void removeOverlay(Node overlayNode, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    glassPane.hideProperty().unbind();
    getChildren().remove(overlayNode);
  }
}
