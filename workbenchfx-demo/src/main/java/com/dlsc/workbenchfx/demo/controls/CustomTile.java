package com.dlsc.workbenchfx.demo.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import javafx.scene.control.Skin;

public class CustomTile extends Tile {
  /**
   * Constructs a new {@link CustomTile}.
   *
   * @param workbench which created this {@link CustomTile}
   */
  public CustomTile(Workbench workbench) {
    super(workbench);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new CustomTileSkin(this);
  }
}
