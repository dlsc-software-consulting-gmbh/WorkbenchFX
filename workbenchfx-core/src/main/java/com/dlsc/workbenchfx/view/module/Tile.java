package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.NavigationDrawerSkin;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;

/**
 * Represents the standard control used to display {@link Module}s as tiles in the home screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tile extends Control {
  private final Workbench workbench;
  private Module module;
  private final Button button;

  /**
   * Constructs a new {@link Tile}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Tile(Workbench workbench) {
    this.workbench = workbench;
    button = new Button();
    button.setText(module.getName());
    button.setGraphic(module.getIcon());
    getStyleClass().add("tile-control");
  }

  /**
   * Defines the {@link EventHandler} which should be called when this tile is being clicked on.
   *
   * @param event to be called
   */
  public void setOnActive(EventHandler<MouseEvent> event) {
    button.setOnMouseClicked(event);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new TileSkin(this);
  }
}
