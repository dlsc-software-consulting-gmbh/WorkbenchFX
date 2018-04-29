package com.dlsc.workbenchfx.custom.calendar;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarView extends BorderPane {

  public CalendarView() {
    getStyleClass().add("module-background");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
    setCenter(new Label("Hello, this is your Calendar!\nToday is the: " + dateFormat.format(new Date())));
  }

}
