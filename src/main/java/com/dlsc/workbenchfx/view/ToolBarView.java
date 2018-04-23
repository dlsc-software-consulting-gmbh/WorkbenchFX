package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View {
  private final WorkbenchFxModel model;
  final Button homeBtn = new Button("Home");

  public ToolBarView(WorkbenchFxModel model) {
    this.model = model;
    init();
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
    getChildren().add(homeBtn);
  }

}
