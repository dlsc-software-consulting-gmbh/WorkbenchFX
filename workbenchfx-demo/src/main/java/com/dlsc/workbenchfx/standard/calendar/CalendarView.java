package com.dlsc.workbenchfx.standard.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CalendarView extends BorderPane {

  private DateFormat dateFormat;
  private Label dateLbl;
  private Timer timer;

  public CalendarView() {
    getStyleClass().add("module-background");

    dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
    dateLbl = new Label();
    setCenter(dateLbl);
  }

  public void startClock() {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(
                () -> dateLbl.setText("Hello, this is your Calendar!\nToday is the: " + dateFormat.format(new Date()))
        );
      }
    }, 0, 1000);
  }

  public void stopClock() {
    if (!Objects.isNull(timer)) {
      timer.cancel();
      timer = null;
    }
  }
}
