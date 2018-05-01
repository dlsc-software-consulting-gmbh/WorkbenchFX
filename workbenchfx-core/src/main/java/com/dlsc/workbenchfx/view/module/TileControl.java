package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.module.Module;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Represents the standard control used to display {@link Module}s as tiles in the home screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class TileControl extends Button {
  private final Module module;

  /**
   * Constructs a new {@link TileControl}.
   *
   * @param module which is used to create the {@link TileControl}
   */
  public TileControl(Module module) {
    this.module = module;
    setText(module.getName());
    setGraphic(module.getIcon());
    getStyleClass().add("tileControl");
  }

  /**
   * Defines the {@link EventHandler} which should be called when this tile is being clicked on.
   *
   * @param event to be called
   */
  public void setOnActive(EventHandler<MouseEvent> event) {
    setOnMouseClicked(event);
  }
}
