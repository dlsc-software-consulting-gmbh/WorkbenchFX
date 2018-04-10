package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.module.Module;
import com.dlsc.workbenchfx.view.View;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MyView extends BorderPane implements View {
  private final Module testModule;
  private final WorkbenchFx workbench;
  private TabControl tabcon;
  private TileControl tilecon;
  private Node testView;

  public MyView(Module testModule, WorkbenchFx workbench) {
    this.testModule = testModule;
    this.workbench = workbench;
    init();
  }

  @Override
  public void initializeParts() {
    tabcon = (TabControl) testModule.getTab();
    tilecon = (TileControl) testModule.getTile();
    testView = testModule.init(workbench);
  }

  @Override
  public void layoutParts() {
    setTop(tabcon);
    setLeft(tilecon);
    setCenter(testView);
  }
}
