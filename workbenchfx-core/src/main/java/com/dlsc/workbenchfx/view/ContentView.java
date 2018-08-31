package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarControl;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Displays the content of the currently active {@link WorkbenchModule}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class ContentView extends BorderPane implements View {

  private static final Logger LOGGER =
      LogManager.getLogger(ContentView.class.getName());

  ToolbarControl toolbarControl;
  AddModuleView addModuleView;
  StackPane moduleViews;

  Node activeView;

  /**
   * Creates a new {@link ContentView}.
   * @param addModuleView containing the created modules as tiles
   */
  public ContentView(AddModuleView addModuleView) {
    this.addModuleView = addModuleView;
    activeView = addModuleView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeSelf() {
    setId("content-view");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeParts() {
    toolbarControl = new ToolbarControl();
    moduleViews = new StackPane(activeView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void layoutParts() {
    setCenter(moduleViews);
    VBox.setVgrow(moduleViews, Priority.ALWAYS);
  }

  /**
   * Replaces the current displayed Node with a new one. This is called when a new module is
   * selected and displayed.
   *
   * @param node the module content as a Node
   */
  final void setContent(Node node) {
    LOGGER.trace("Setting active view to module's view");
    if (!moduleViews.getChildren().contains(node)) {
      LOGGER.trace("Initially add view of module to the scene graph");
      moduleViews.getChildren().add(node);
    }
    activeView = node;
    activeView.setVisible(true);
  }

  final void hideActiveView() {
    activeView.setVisible(false);
    activeView = null;
  }

  final boolean removeView(Node view) {
    return moduleViews.getChildren().remove(view);
  }

  /**
   * Displays the {@link ToolbarControl} based on the given parameter.
   *
   * @param show true if the {@link ToolbarControl} should be displayed, false if not
   */
  final void showToolbar(boolean show) {
    setTop(show ? toolbarControl : null);
  }

  final void setAddModuleView() {
    LOGGER.trace("Setting active view to addModuleView");
    activeView = addModuleView;
    addModuleView.setVisible(true);
  }
}
