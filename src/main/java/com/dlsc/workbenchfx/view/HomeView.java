package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import com.dlsc.workbenchfx.view.module.TileControl;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.HBox;

public class HomeView extends HBox implements View {
  private final WorkbenchFxModel model;
  Map<TileControl, Module> moduleMap;

  public HomeView(WorkbenchFxModel model) {
    this.model = model;
    moduleMap = new HashMap<>();
    init();
  }

  @Override
  public void initializeParts() {

  }

  @Override
  public void layoutParts() {
    // Adds the module-tiles to the view
    model.getModules().forEach(module -> {
      TileControl tileControl = (TileControl) module.getTile();
      moduleMap.put(tileControl, module);
      getChildren().add(tileControl);
    });
  }
}
