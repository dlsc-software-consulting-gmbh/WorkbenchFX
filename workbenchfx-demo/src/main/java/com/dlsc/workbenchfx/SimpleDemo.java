package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.modules.preferences.PreferencesModule;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleDemo extends Application {

  public Workbench workbench;
  private PreferencesModule preferencesModule = new PreferencesModule();
  private CalendarModule calendarModule = new CalendarModule();
  private HelloWorldModule helloWorldModule = new HelloWorldModule();

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
        helloWorldModule,
        preferencesModule
    ).build();
    return workbench;
  }
}
