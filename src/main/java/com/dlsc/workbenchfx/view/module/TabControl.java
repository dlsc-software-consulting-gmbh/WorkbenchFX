package com.dlsc.workbenchfx.view.module;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class TabControl extends HBox {
  private final String name;
  private final Node icon;
  private final Button closeBtn = new Button("x");

  public TabControl(String name, Node icon) {
    this.name = name;
    this.icon = icon;
    getChildren().addAll(
        new Label(name),
        closeBtn
    );
  }

  public void setOnCloseRequest(EventHandler<ActionEvent> event) {
    closeBtn.setOnAction(event);
  }

  public void setOnActiveRequest(EventHandler<MouseEvent> event) {
    setOnMouseClicked(event);
  }
}
