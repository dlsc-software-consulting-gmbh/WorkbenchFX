package com.dlsc.workbenchfx.testing;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class MockTileSkin extends SkinBase<MockTile> {
  public MockTileSkin(MockTile mockTile) {
    super(mockTile);
    getChildren().add(new Label());
  }
}
