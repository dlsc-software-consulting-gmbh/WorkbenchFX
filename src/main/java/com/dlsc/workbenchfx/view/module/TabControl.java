package com.dlsc.workbenchfx.view.module;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

public class TabControl extends Button {
  private final String name;
  private final Node icon;

  public TabControl(String name, Node icon) {
    this.name = name;
    this.icon = icon;
    setText(name + " Tab");
    setGraphic(icon);
  }

}
