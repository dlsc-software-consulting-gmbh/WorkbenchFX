package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.view.controls.ToolbarControl;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Displays the content of the currently active {@link Module}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ContentView extends VBox implements View {

  ToolbarControl moduleToolbarControl;

  /**
   * Creates a new {@link ContentView}.
   */
  public ContentView() {
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("content-view");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    moduleToolbarControl = new ToolbarControl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * Replaces the current displayed Node with a new one. This is called when a new module is
   * selected and displayed.
   *
   * @param node the module content as a Node
   */
  public void setContent(Node node) {
    getChildren().setAll(node);
  }

  void addToolbar() {
    if (!getChildren().contains(moduleToolbarControl)) {
      getChildren().add(0, moduleToolbarControl);
    }
  }
}
