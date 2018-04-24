package com.dlsc.workbenchfx.extended.preferences;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class PreferencesView extends BorderPane {

  public PreferencesView() {
    setCenter(new Label("Hello, those are your Preferences! You have nothing defined."));
  }
}
