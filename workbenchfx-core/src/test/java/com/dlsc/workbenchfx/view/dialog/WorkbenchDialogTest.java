package com.dlsc.workbenchfx.view.dialog;

import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkbenchDialogTest {

  private static final String TITLE = "Dialog Test Title";
  private static final String MESSAGE = "Dialog Test Message";
  private static final Label CONTENT = new Label(MESSAGE);
  private static final WorkbenchDialog.Type TYPE = WorkbenchDialog.Type.INFORMATION;
  private WorkbenchDialog dialog;

  @BeforeEach
  void setUp() {
    dialog = WorkbenchDialog.builder(TITLE, CONTENT, TYPE).build();
  }

  @Test
  void testCtor() {
    WorkbenchDialog dialogConfirmation =
        WorkbenchDialog.builder(TITLE, CONTENT, WorkbenchDialog.Type.CONFIRMATION).build();
    WorkbenchDialog dialogMessage = WorkbenchDialog.builder(TITLE, MESSAGE, TYPE).build();

  }

}
