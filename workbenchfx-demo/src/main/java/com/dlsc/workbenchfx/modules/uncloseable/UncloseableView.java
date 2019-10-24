package com.dlsc.workbenchfx.modules.uncloseable;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class UncloseableView extends BorderPane {

  public UncloseableView() {
    getStyleClass().add("module-background");
    setCenter(new Label("This module cannot be closed."));
  }

}

