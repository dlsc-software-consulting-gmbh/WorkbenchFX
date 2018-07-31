package com.dlsc.workbenchfx.testing;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class MockSelectionStripSkin extends SkinBase<MockSelectionStrip> {

  public MockSelectionStripSkin(MockSelectionStrip mockSelectionStrip) {
    super(mockSelectionStrip);
    getChildren().add(new Label());
  }
}
