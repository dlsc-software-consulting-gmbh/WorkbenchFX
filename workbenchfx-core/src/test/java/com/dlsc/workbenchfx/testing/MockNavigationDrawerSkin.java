package com.dlsc.workbenchfx.testing;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class MockNavigationDrawerSkin extends SkinBase<MockNavigationDrawer> {
  public MockNavigationDrawerSkin(MockNavigationDrawer mockNavigationDrawer) {
    super(mockNavigationDrawer);
    getChildren().add(new Label());
  }
}
