package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.modules.notes.NotesModule;
import com.dlsc.workbenchfx.modules.preferences.PreferencesModule;
import com.dlsc.workbenchfx.modules.webview.WebModule;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleDemo extends Application {

  public Workbench workbench;
  private PreferencesModule preferencesModule = new PreferencesModule();
  private CalendarModule calendarModule = new CalendarModule();
  private NotesModule notesModule = new NotesModule();
  private WebModule dlsc = new WebModule("DLSC", "http://dlsc.com");
  private WebModule documentation = new WebModule("Documentation",WebModule .class.getResource("index.html").toExternalForm());
  private WebModule notepad = new WebModule("Notepad", "https://docs.google.com");

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("Simple WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();

    // open calendar module by default
    workbench.openModule(calendarModule);
  }

  private Workbench initWorkbench() {
    workbench = Workbench.builder(
        calendarModule,
        notesModule,
        preferencesModule,
        dlsc,
        notepad,
        documentation
    ).build();
    return workbench;
  }
}
