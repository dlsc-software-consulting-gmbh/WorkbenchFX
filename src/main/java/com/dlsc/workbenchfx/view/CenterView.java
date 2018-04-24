package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class CenterView extends StackPane implements View {
  private final WorkbenchFx model;

  public CenterView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("center");
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
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

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
