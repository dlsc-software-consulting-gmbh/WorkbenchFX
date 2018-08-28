package com.dlsc.workbenchfx.modules.test;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class InterruptClosingTestModule extends WorkbenchModule {

  boolean closePossible;

  public InterruptClosingTestModule() {
    super("Interrupt Close", MaterialDesignIcon.HELP);
  }

  @Override
  public void init(Workbench workbench) {
    super.init(workbench);
    closePossible = false;
  }

  @Override
  public Node activate() {
    return new Label("This module will open up a dialog when trying to close it.");
  }

  @Override
  public boolean destroy() {
    System.out.println("DESTROY CALLED ON 1");

    getWorkbench().showDialog(WorkbenchDialog.builder("Confirmation",
        "Are you sure you want to close this module without saving?",
        WorkbenchDialog.Type.CONFIRMATION)
        .blocking(true)
        .onResult(buttonType -> {
          if (ButtonType.YES.equals(buttonType)) {
            System.out.println("Pressed: YES");
            close();
          }
        })
        .build());
    return false;
  }
}
