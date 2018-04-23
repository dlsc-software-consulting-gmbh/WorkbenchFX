package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
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
    setId("toolbar");

    homeBtn = new Button("Home");
    homeBtn.setId("homeButton");

    tabBox = new HBox();
    tabBox.setId("tabBox");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().addAll(homeBtn, tabBox);
  }

  public void addTab(Node tab) {
    tabBox.getChildren().add(tab);
  }

  public void removeTab(int index) {
    tabBox.getChildren().remove(index);
  }

}
