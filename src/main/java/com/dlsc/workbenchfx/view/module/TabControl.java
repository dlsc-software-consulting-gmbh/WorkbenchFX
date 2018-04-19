package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.model.module.Module;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class TabControl extends HBox {
  private final Button closeBtn = new Button("x");
  private Module module;

  public TabControl(Module module) {
    this.module = module;
    getChildren().addAll(
        new Label(module.getName()),
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
