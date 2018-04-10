package com.dlsc.workbenchfx.view.module;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class TileControl extends Button {
  private final String name;
  private final Node icon;

  public TileControl(String name, Node icon) {
    this.name = name;
    this.icon = icon;
    setText(name);
    setGraphic(icon);
  }

}
