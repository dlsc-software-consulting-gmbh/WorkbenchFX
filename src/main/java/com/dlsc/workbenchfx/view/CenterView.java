package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class CenterView extends StackPane implements View {
  private final WorkbenchFxModel model;

  public CenterView(WorkbenchFxModel model) {
    this.model = model;
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
  public void setContentNode(Node node) {
    getChildren().clear();
    getChildren().add(node);
  }
}
