package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.concurrent.CompletableFuture;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class InterruptClosingTestModule extends WorkbenchModule {

  boolean closePossible;

  public InterruptClosingTestModule() {
    super("Interrupt Close", FontAwesomeIcon.QUESTION);
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
  public boolean destroy(CompletableFuture<Boolean> stageCloseable) {
      getWorkbench().openModule(this);
      CompletableFuture<ButtonType> dialogResult =
          getWorkbench().showConfirmationDialog("Confirmation",
              "Are you sure you want to close this module without saving?");
      dialogResult.thenAccept(buttonType -> {
        if (ButtonType.YES.equals(buttonType)) {
          stageCloseable.complete(true);
        } else {
          stageCloseable.complete(false);
        }
      });
      return false;
  }
}
