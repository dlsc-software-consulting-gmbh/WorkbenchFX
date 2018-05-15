package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link Module}s as tiles in the home screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tile extends Control {
  private static final Logger LOGGER = LogManager.getLogger(Tile.class.getName());

  private final Workbench workbench;
  private final ObjectProperty<Module> module;

  /**
   * Constructs a new {@link Tile}.
   *
   * @param workbench which created this {@link Tile}
   */
  public Tile(Workbench workbench) {
    this.workbench = workbench;
    module = new SimpleObjectProperty<>();
  }

  /**
   * Defines the {@code module} which is being represented by this {@link Tile}.
   *
   * @param module to be represented by this {@link Tile}
   */
  public void update(Module module) {
    LOGGER.trace("Setting reference to module");
    this.module.set(module);
  }

  public Module getModule() {
    return module.get();
  }

  public ReadOnlyObjectProperty<Module> moduleProperty() {
    return module;
  }

  public Workbench getWorkbench() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new TileSkin(this);
  }
}
