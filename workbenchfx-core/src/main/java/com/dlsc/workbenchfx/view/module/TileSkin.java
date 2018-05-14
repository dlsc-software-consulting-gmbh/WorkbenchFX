package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;

/**
 * TODO
 */
public class TileSkin extends SkinBase<Tile> {

  private final Button button;
  private final ReadOnlyObjectProperty<Module> module;

  /**
   * Creates a new {@link TileSkin} object for a corresponding {@link Tile}.
   *
   * @param tile the {@link Tile} for which this Skin is created
   */
  public TileSkin(Tile tile) {
    super(tile);
    module = tile.moduleProperty();

    button = new Button();
    button.getStyleClass().add("tile-control");

    setupModuleListener(tile.getWorkbench());
  }

  private void setupModuleListener(Workbench workbench) {
    module.addListener((observable, oldModule, newModule) -> {
      if (oldModule != newModule) {
        button.setText(newModule.getName());
        button.setGraphic(newModule.getIcon());
        button.setOnAction(e -> workbench.openModule(newModule));
      }
    });
  }

}
