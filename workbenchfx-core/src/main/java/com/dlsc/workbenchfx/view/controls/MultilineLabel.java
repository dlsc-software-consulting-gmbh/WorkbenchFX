package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.model.WorkbenchDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Represents the standard control used to display the message of a {@link WorkbenchDialog}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class MultilineLabel extends Label {

  String message;

  /**
   * Creates a label to be used as the content of a {@link WorkbenchDialog}.
   *
   * @param message of the dialog
   */
  public MultilineLabel(String message) {
    this.message = message;
    setText(message);
    setWrapText(true); // makes sure long text doesn't get cut off at the end of a dialog
    VBox.setVgrow(this, Priority.ALWAYS); // makes sure long text can grow in the dialog
    HBox.setHgrow(this, Priority.ALWAYS);
    setMaxWidth(Double.MAX_VALUE);
  }

  public String getMessage() {
    return message;
  }
}
