package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class HomeView extends StackPane implements View {
  private final WorkbenchFxModel model;
  final Module[] modules;
  private final WorkbenchFx workbench;

  public HomeView(WorkbenchFxModel model, Module[] modules, WorkbenchFx workbench) {
    this.model = model;
    this.modules = modules;
    this.workbench = workbench;
    init();
  }

  @Override
  public void initializeParts() {

  }

  @Override
  public void layoutParts() {
    getChildren().add(modules[0].getTile());
  }
}
