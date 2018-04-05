package com.dlsc.workbenchfx.view.module;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.image.Image;

public class TabControl extends Control {
  private final String name;
  private final Node icon;

  public TabControl(String name, Node icon) {
    this.name = name;
    this.icon = icon;
  }
}
