package com.dlsc.workbenchfx.testing;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class MockTabSkin extends SkinBase<MockTab> {
  public MockTabSkin(MockTab mockTab) {
    super(mockTab);
    getChildren().add(new Label());
  }
}
