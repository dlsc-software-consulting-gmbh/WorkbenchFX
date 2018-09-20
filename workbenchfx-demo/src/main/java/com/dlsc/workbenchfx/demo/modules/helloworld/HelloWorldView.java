package com.dlsc.workbenchfx.demo.modules.helloworld;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class HelloWorldView extends BorderPane {

  public HelloWorldView() {
    getStyleClass().add("module-background");
    setCenter(new Label("My first workbench module."));
  }

}
