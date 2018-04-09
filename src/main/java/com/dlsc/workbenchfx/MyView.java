package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.view.View;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MyView extends BorderPane implements View {
  private final TestModule testModule;
  private final WorkbenchFx workbench;
  private TabControl tabcon;
  private TileControl tilecon;
  private TestView testView;

  public MyView(TestModule testModule, WorkbenchFx workbench) {
    this.testModule = testModule;
    this.workbench = workbench;
    init();
  }

  @Override
  public void initializeParts() {
    tabcon = (TabControl) testModule.getTab();
    tilecon = (TileControl) testModule.getTile();
    testView = (TestView) testModule.init(workbench);
  }

  @Override
  public void layoutParts() {
    setTop(tabcon);
    setLeft(tilecon);
    setCenter(testView);
  }
}
