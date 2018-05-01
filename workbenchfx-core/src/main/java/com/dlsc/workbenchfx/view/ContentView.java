package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Displays the content of the currently active {@link Module}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ContentView extends StackPane implements View {
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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * Replaces the current displayed Node with a new one.
   * This is called when a new module is selected and displayed.
   *
   * @param node the module content as a Node
   */
  public void setContent(Node node) {
    getChildren().clear();
    getChildren().add(node);
  }
}
