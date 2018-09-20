package com.dlsc.workbenchfx.demo;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.helloworld.HelloWorldModule;
import javafx.fxml.FXML;

public class FXMLController {

  @FXML
  private Workbench workbench;

  @FXML
  private void initialize() {
    workbench.getModules().add(new HelloWorldModule());
  }

}
