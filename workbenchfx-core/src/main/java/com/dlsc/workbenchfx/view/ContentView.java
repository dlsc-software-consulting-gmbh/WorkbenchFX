package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarControl;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Displays the content of the currently active {@link WorkbenchModule}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class ContentView extends BorderPane implements View {

  private static final Logger LOGGER = LogManager.getLogger(ContentView.class.getName());
  ToolbarControl toolbarControl;
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
  public final void initializeSelf() {
    setId("content-view");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeParts() {
    toolbarControl = new ToolbarControl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void layoutParts() {

  }

  /**
   * Replaces the current displayed Node with a new one. This is called when a new module is
   * selected and displayed.
   *
   * @param node the module content as a Node
   */
  final void setContent(Node node) {
    setCenter(node);
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
    setCenter(addModuleView);
  }
}
