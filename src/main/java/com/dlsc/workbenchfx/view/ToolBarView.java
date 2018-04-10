package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View{
  private final WorkbenchFxModel model;
  private final Module[] modules;
  private final WorkbenchFx workbench;
  final Button homeBtn = new Button("Home");

  public ToolBarView(WorkbenchFxModel model, Module[] modules, WorkbenchFx workbench) {
    this.model = model;
    this.modules = modules;
    this.workbench = workbench;
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
    getChildren().add(
        homeBtn
    );
  }

  public void trigger(Node tab) {
    if(!getChildren().contains(tab)){
      getChildren().add(tab);
    }
  }
}
