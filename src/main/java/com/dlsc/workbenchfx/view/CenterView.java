package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class CenterView extends StackPane implements View {
  private final WorkbenchFx model;

  public CenterView(WorkbenchFx model) {
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

  public void setContentNode(Node node) {
    getChildren().clear();
    getChildren().add(node);
  }
}
