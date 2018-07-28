package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.view.controls.ToolbarControl;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Displays the content of the currently active {@link Module}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ContentView extends BorderPane implements View {

  private static final Logger LOGGER = LogManager.getLogger(ContentView.class.getName());
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
    setCenter(node);
  }

  void addToolbar() {
    setTop(moduleToolbarControl);
  }
}
