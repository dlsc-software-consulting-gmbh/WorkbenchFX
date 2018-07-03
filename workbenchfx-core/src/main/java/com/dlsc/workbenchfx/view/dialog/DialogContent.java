package com.dlsc.workbenchfx.view.dialog;

import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Represents the standard control used to display the message of a {@link WorkbenchDialog}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DialogContent extends Label {

  /**
   * Creates a label to be used as the content of a {@link WorkbenchDialog}.
   * @param message of the dialog
   */
  public DialogContent(String message) {
    setText(message);
    setWrapText(true);
    VBox.setVgrow(this,Priority.ALWAYS);
  }

}
