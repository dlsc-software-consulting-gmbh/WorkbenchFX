package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO.
 */
public class TileSkin extends SkinBase<Tile> {
  private static final Logger LOGGER = LogManager.getLogger(TileSkin.class.getName());

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

    Workbench workbench = tile.getWorkbench();
    setupSkin(workbench, module.get()); // initial setup
    setupModuleListener(workbench); // setup for changing modules

    getChildren().add(button);
  }

  private void setupModuleListener(Workbench workbench) {
    LOGGER.trace("Add module listener");
    module.addListener((observable, oldModule, newModule) -> {
      LOGGER.trace("moduleListener called");
      LOGGER.trace("old: " + oldModule + " new: " + newModule);
      if (oldModule != newModule) {
        LOGGER.trace("Setting up skin");
        setupSkin(workbench, newModule);
      }
    });
  }

  private void setupSkin(Workbench workbench, Module module) {
    button.setText(module.getName());
    button.setGraphic(module.getIcon());
    button.setOnAction(e -> workbench.openModule(module));
  }
}
