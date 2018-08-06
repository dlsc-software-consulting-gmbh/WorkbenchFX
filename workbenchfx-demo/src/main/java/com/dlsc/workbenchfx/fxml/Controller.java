package com.dlsc.workbenchfx.fxml;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.fxml.notes.NotesModule;
import javafx.fxml.FXML;

public class Controller {

  @FXML
  private Workbench workbench;

  @FXML
  private void initialize() {
    workbench.getModules().add(new NotesModule());
  }

}
