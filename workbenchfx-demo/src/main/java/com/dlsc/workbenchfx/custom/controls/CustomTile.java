package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.module.Tile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link Module}s as customTiles in the home
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
