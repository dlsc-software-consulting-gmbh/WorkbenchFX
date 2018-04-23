package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.module.Module;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class TileControl extends Button {
  private Module module;

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

  public void setOnActive(EventHandler<MouseEvent> event) {
    setOnMouseClicked(event);
  }
}
