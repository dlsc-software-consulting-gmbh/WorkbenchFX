package com.dlsc.workbenchfx.modules.notes;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class NotesView extends BorderPane {

  public NotesView() {
    getStyleClass().add("module-background");
    setCenter(new Label("Hello, here are your Notes! You have nothing written yet."));
  }

}
