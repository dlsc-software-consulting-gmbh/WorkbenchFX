package com.dlsc.workbenchfx.extended;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.extended.calendar.CalendarModule;
import com.dlsc.workbenchfx.extended.notes.NotesModule;
import com.dlsc.workbenchfx.extended.preferences.PreferencesModule;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ExtendedDemo extends Application {

  public WorkbenchFx workbenchFx;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("Extended WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

  private WorkbenchFx initWorkbench() {
    return workbenchFx = WorkbenchFx.builder(
        new PreferencesModule(),
        new PreferencesModule(),
        new PreferencesModule(),
        new PreferencesModule(),
        new PreferencesModule(),
        new CalendarModule(),
        new CalendarModule(),
        new CalendarModule(),
        new CalendarModule(),
        new CalendarModule(),
        new NotesModule(),
        new NotesModule(),
        new NotesModule(),
        new NotesModule(),
        new NotesModule()
    ).build();
  }

}
