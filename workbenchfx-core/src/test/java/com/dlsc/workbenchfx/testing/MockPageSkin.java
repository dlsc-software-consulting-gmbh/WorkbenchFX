package com.dlsc.workbenchfx.testing;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class MockPageSkin extends SkinBase<MockPage> {
  public MockPageSkin(MockPage mockPage) {
    super(mockPage);
    getChildren().add(new Label());
  }
}
