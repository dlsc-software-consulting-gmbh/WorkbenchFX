package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.module.Module;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;

/**
 * Represents the standard control used to display {@link Module}s as tiles in the home screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tile extends Control {
  private final Module module;
  private final Button button;

  /**
   * Constructs a new {@link Tile}.
   *
   * @param module which is used to create the {@link Tile}
   */
  public Tile(Module module) {
    this.module = module;
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
}
