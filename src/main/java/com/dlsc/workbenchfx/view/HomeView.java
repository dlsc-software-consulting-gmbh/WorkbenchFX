package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class HomeView extends HBox implements View {
  private final WorkbenchFxModel model;

  public HomeView(WorkbenchFxModel model) {
    this.model = model;
    init();
  }

  @Override
  public void initializeParts() {

  }

  @Override
  public void layoutParts() {
    model.getModules().forEach(module -> getChildren().add(module.getTile()));
  }
}
