package com.dlsc.workbenchfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLDemo extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("workbench.fxml"));
    primaryStage.setTitle("WorkbenchFX");
    primaryStage.setScene(new Scene(root, 1000, 700));
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
