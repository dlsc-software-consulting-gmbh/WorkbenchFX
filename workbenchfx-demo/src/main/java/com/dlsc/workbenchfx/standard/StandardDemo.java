package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.extended.calendar.CalendarModule;
import com.dlsc.workbenchfx.extended.notes.NotesModule;
import com.dlsc.workbenchfx.extended.preferences.PreferencesModule;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StandardDemo extends Application {

  public Workbench workbench;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("Standard WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

  private Workbench initWorkbench() {
    return workbench = Workbench.builder(
        new PreferencesModule(),
        new CalendarModule(),
        new NotesModule()
    ).build();
  }

}
