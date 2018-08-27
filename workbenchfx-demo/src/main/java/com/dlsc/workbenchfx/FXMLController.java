package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.notes.NotesModule;
import javafx.fxml.FXML;

public class FXMLController {

  @FXML
  private Workbench workbench;

  @FXML
  private void initialize() {
    workbench.getModules().add(new NotesModule());
  }

}
