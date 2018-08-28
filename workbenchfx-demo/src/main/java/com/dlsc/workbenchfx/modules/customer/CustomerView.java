package com.dlsc.workbenchfx.modules.customer;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CustomerView extends BorderPane {

  public CustomerView() {
    getStyleClass().add("module-background");
    setCenter(new Label("Hello, here are your Customers! You don't have any entered yet."));
  }

}
