package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class HomeView extends GridPane implements View {
  private final WorkbenchFxModel model;
  private final Module[] modules;
  private final WorkbenchFx workbench;

  public HomeView(WorkbenchFxModel model, Module[] modules, WorkbenchFx workbench) {
    this.model = model;
    this.modules = modules;
    this.workbench = workbench;
  }

  @Override
  public void initializeParts() {

  }

  @Override
  public void layoutParts() {

  }
}
