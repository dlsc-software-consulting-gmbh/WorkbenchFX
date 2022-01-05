package com.dlsc.workbenchfx.demo;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.demo.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.demo.modules.maps.MapsModule;
import com.dlsc.workbenchfx.demo.modules.webview.WebModule;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class SimpleDemo extends Application {

  private Workbench workbench;
  private CalendarModule calendarModule = new CalendarModule();
  private HelloWorldModule helloWorldModule = new HelloWorldModule();
  private WebModule dlsc = new WebModule("JFX-Central",  MaterialDesign.MDI_WEB,"https://jfx-central.com");
  private WebModule notepad = new WebModule("Notepad", MaterialDesign.MDI_NOTE, "https://docs.google.com");
  private MapsModule mapsModule = new MapsModule();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    CSSFX.start(myScene);

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
        mapsModule,
        dlsc,
        notepad
    ).build();
    return workbench;
  }
}
