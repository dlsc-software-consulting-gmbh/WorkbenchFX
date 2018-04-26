package com.dlsc.workbenchfx.extended.calendar;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CalendarView extends BorderPane {

  public CalendarView() {
    getStyleClass().add("module-background");
    setCenter(new Label("Hello, this is your Calendar! Today is the 10/04/2018."));
  }

}
