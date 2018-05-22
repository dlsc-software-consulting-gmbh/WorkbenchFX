package com.dlsc.workbenchfx.testing;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import javafx.scene.control.Skin;

public class MockTile extends Tile {
  public MockTile(Workbench workbench) {
    super(workbench);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new MockTileSkin(this);
  }
}
