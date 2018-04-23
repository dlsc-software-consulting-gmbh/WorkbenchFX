package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View {
  private final WorkbenchFx model;
  final Button homeBtn = new Button("Home");

  public ToolBarView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    homeBtn.getStyleClass().add("tabControl");
    setSpacing(10);
    getChildren().add(homeBtn);
  }

}
