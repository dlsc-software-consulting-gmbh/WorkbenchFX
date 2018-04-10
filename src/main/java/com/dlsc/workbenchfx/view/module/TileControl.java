package com.dlsc.workbenchfx.view.module;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class TileControl extends Button {
  private final String name;
  private final Node icon;

  public TileControl(String name, Node icon) {
    this.name = name;
    this.icon = icon;
    setText(name + " Tile");
    setGraphic(icon);
  }

}
