package com.dlsc.workbenchfx.custom.controls;

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
public class CustomTileSkin extends SkinBase<CustomTile> {
  private static final Logger LOGGER = LogManager.getLogger(
      CustomTileSkin.class.getName());

  private final Button button;
  private final ReadOnlyObjectProperty<Module> module;

  /**
   * Creates a new {@link CustomTileSkin} object for a corresponding {@link CustomTile}.
   *
   * @param customTile the {@link CustomTile} for which this Skin is created
   */
  public CustomTileSkin(CustomTile customTile) {
    super(customTile);
    module = customTile.moduleProperty();

    button = new Button();
    button.getStyleClass().add("tile-control");

    Workbench workbench = customTile.getWorkbench();
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
