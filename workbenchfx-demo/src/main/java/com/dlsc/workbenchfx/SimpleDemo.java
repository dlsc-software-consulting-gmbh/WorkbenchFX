package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.modules.gantt.GanttModule;
import com.dlsc.workbenchfx.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.modules.maps.MapsModule;
import com.dlsc.workbenchfx.modules.preferences.PreferencesModule;
import com.dlsc.workbenchfx.modules.webview.WebModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleDemo extends Application {

  public Workbench workbench;
  private PreferencesModule preferencesModule = new PreferencesModule();
  private CalendarModule calendarModule = new CalendarModule();
  private HelloWorldModule helloWorldModule = new HelloWorldModule();
  private GanttModule ganttModule = new GanttModule();
  private WebModule dlsc = new WebModule("DLSC",  MaterialDesignIcon.WEB,"http://dlsc.com");
  private WebModule notepad = new WebModule("Notepad", MaterialDesignIcon.NOTE, "https://docs.google.com");
  private MapsModule mapsModule = new MapsModule();

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
        preferencesModule,
        ganttModule,
        dlsc,
        notepad,
        mapsModule
    ).build();
    return workbench;
  }
}
