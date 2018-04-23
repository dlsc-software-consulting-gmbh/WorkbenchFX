package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View {
  private final WorkbenchFx model;
  Button homeBtn;
  HBox tabBox;

  public ToolBarView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    homeBtn = new Button("Home");
    tabBox = new HBox();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    homeBtn.setId("homeButton");
    setSpacing(10);
    getChildren().addAll(homeBtn, tabBox);
  }

}
