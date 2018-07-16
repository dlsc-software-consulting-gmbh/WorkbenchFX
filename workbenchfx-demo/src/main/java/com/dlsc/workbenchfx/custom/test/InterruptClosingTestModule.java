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
  public boolean destroy() {
    if (!closePossible) {
      getWorkbench().openModule(this);
      getWorkbench().showConfirmationDialog("Confirmation", "Are you sure you want to close this module without saving?", buttonType -> {
        if (ButtonType.YES.equals(buttonType)) {
          closePossible = true;
          getWorkbench().closeModule(this);
        }
      });
      return false;
    } else {
      return true;
    }
  }

  public boolean isClosePossible() {
    return closePossible;
  }

  public void setClosePossible(boolean closePossible) {
    this.closePossible = closePossible;
  }
}
