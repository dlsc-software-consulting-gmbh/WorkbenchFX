package com.dlsc.workbenchfx.testing;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class MockDialogSkin extends SkinBase<MockDialogControl> {
  public MockDialogSkin(MockDialogControl mockDialogControl) {
    super(mockDialogControl);
    getChildren().add(new Label());
  }
}
