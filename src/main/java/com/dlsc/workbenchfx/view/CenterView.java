package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class CenterView extends StackPane implements View {
  private final WorkbenchFxModel model;
  private final Module[] modules;
  private final WorkbenchFx workbench;

  public CenterView(WorkbenchFxModel model, Module[] modules, WorkbenchFx workbench) {
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

  public void setContentNode(Node node) {
    getChildren().clear();
    getChildren().add(node);
  }
}
