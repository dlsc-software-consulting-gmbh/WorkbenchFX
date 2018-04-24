package com.dlsc.workbenchfx;

// Change this import depending on the demo
import com.dlsc.workbenchfx.standard.DemoPane;
//import com.dlsc.workbenchfx.custom.DemoPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppStarter extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    DemoPane demoPane = new DemoPane();
    Scene myScene = new Scene(demoPane);

    primaryStage.setTitle("WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }
}
