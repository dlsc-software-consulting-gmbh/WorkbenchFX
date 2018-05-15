package com.dlsc.workbenchfx.testing;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.scene.control.Skin;

public class MockTab extends Tab {
  public MockTab(Workbench workbench) {
    super(workbench);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new MockTabSkin(this);
  }
}
