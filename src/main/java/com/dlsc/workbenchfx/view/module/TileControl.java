package com.dlsc.workbenchfx.view.module;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class TileControl extends Button {
  private final String name;
  private final Node icon;

  public TileControl(String name, Node icon) {
    this.name = name;
    this.icon = icon;
    setText(name);
    setGraphic(icon);
  }

  public void setOnActiveRequest(EventHandler<MouseEvent> event) {
    setOnMouseClicked(event);
  }
}
