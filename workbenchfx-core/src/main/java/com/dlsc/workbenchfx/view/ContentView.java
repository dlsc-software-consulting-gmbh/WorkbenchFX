package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.view.controls.WorkbenchToolbar;
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
  WorkbenchToolbar workbenchToolbar;
  AddModuleView addModuleView;

  /**
   * Creates a new {@link ContentView}.
   */
  public ContentView(AddModuleView addModuleView) {
    this.addModuleView = addModuleView;
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
    workbenchToolbar = new WorkbenchToolbar();
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
  void setContent(Node node) {
    setCenter(node);
  }

  /**
   * Displays the {@link WorkbenchToolbar} based on the given parameter.
   *
   * @param show true if the {@link WorkbenchToolbar} should be displayed, false if not
   */
  void showToolbar(boolean show) {
    setTop(show ? workbenchToolbar : null);
  }

  void setAddModuleView() {
    setCenter(addModuleView);
  }
}
