package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import javafx.scene.control.Skin;

/**
 * Represents the standard control used to display {@link WorkbenchModule}s as customTiles in the home
 * screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
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
