package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.modules.webview.WebModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NoAddModuleBtnDemo extends Application {

  private Workbench workbench;
  private HelloWorldModule helloWorldModule = new HelloWorldModule();
  private WebModule dlsc = new WebModule("DLSC",  MaterialDesignIcon.WEB,"http://dlsc.com");

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("NoAddModuleBtn WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

  private Workbench initWorkbench() {
    workbench = Workbench.builder(
        helloWorldModule,
        dlsc
    ).build();
    workbench.setAddModuleBtnVisible(false);
    return workbench;
  }

}
