package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import javafx.scene.layout.HBox;

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
    // Adds the module-tiles to the view
    model.getModules().forEach(module -> getChildren().add(module.getTile()));
  }
}
