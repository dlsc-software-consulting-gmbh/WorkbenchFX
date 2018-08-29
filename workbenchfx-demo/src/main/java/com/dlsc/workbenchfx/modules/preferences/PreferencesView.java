package com.dlsc.workbenchfx.modules.preferences;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class PreferencesView extends BorderPane {

  public PreferencesView() {
    getStyleClass().add("module-background");
    setCenter(new Label("Hello, those are your Preferences! You have nothing defined."));
  }
}
