package com.dlsc.workbenchfx.view.controls.dialog;

import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.google.common.base.Strings;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

/**
 * Represents the standard control used to display a {@link WorkbenchDialog} of error type.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DialogErrorContent extends VBox {

  private final Node message;
  private final String details;

  /**
   * Creates a control which shows the details of an exception or error to be used as the content of
   * a {@link WorkbenchDialog}.
   *
   * @param message the {@link Node} containing the standard dialog message
   * @param details about the error or exception
   */
  public DialogErrorContent(Node message, String details) {
    this.message = message;
    this.details = details;

    getStyleClass().add("container");

    // add message to the dialog content
    getChildren().add(message);

    // if details were specified, add them wrapped in a TitledPane
    if (!Strings.isNullOrEmpty(details)) {
      TextArea textArea = new TextArea();
      textArea.setText(details);
      textArea.setWrapText(true);
      textArea.getStyleClass().add("error-details-text-area");

      TitledPane titledPane = new TitledPane();
      titledPane.getStyleClass().add("error-details-titled-pane");
      titledPane.setText("Details");
      titledPane.setContent(textArea);
      titledPane.setPrefHeight(300);

      getChildren().add(titledPane);
    }

  }

  public final Node getMessage() {
    return message;
  }

  public final String getDetails() {
    return details;
  }
}
