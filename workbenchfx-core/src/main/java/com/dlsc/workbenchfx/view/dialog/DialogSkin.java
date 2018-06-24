package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import com.dlsc.workbenchfx.view.controls.DropdownSkin;
import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogSkin extends SkinBase<DialogControl> {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogSkin.class.getName());

  /**
   * Creates a new {@link DialogSkin} object for a corresponding {@link DialogControl}.
   *
   * @param dialogControl the {@link DialogControl} for which this Skin is created
   */
  public DialogSkin(DialogControl dialogControl) {
    super(dialogControl);
  }

  private void initDialog(Workbench workbench) {
    dialogPane = new VBox();
    dialogPane.setFillWidth(true);
    dialogPane.getStyleClass().add("dialog-pane");
    //dialogPane.setVisible(false);

    HBox dialogHeader = new HBox();
    dialogHeader.setAlignment(Pos.CENTER_LEFT);
    dialogHeader.getStyleClass().add("dialog-header");

    dialogTitle = new Label("Dialog");
    dialogTitle.setMaxWidth(Double.MAX_VALUE);
    dialogTitle.getStyleClass().add("dialog-title");

    Button dialogCloseButton = new Button();
    dialogCloseButton.setOnAction(evt -> {
      workbench.getDialog().getResult().complete(ButtonType.CANCEL);
      workbench.hideDialog();
    });
    dialogCloseButton.getStyleClass().addAll("dialog-close-button", "dialog-close-icon");

    HBox.setHgrow(dialogTitle, Priority.ALWAYS);
    HBox.setHgrow(dialogCloseButton, Priority.NEVER);

    dialogHeader.getChildren().setAll(dialogTitle, dialogCloseButton);

    dialogContentPane = new StackPane();
    dialogContentPane.getStyleClass().add("dialog-content-pane");

    dialogButtonBar = new ButtonBar();
    dialogButtonBar.managedProperty().bind(dialogButtonBar.visibleProperty());

    VBox.setVgrow(dialogTitle, Priority.NEVER);
    VBox.setVgrow(dialogContentPane, Priority.ALWAYS);
    VBox.setVgrow(dialogButtonBar, Priority.NEVER);

    dialogPane.getChildren().setAll(dialogHeader, dialogContentPane, dialogButtonBar);
  }

  private void showDialog() {
    dialogPane.setVisible(true);
    WorkbenchDialog workbenchDialog = getSkinnable().getDialog();
    dialogTitle.textProperty().bind(workbenchDialog.titleProperty());
    dialogContentPane.getChildren().setAll(workbenchDialog.getContent());
    dialogPane.getStyleClass().setAll("dialog-pane");
    dialogPane.getStyleClass().addAll(workbenchDialog.getStyleClass());

    updateButtons(workbenchDialog);

    if (!getChildren().contains(dialogPane)) {
      getChildren().add(dialogPane);
    }
  }

  private void hideDialog() {
    dialogPane.setVisible(false);
  }

  private void updateButtons(WorkbenchDialog<?> dialog) {
    dialogButtonBar.getButtons().clear();
    dialogButtonBar.setVisible(dialog.isShowButtonsBar());

    boolean hasDefault = false;
    for (ButtonType cmd : dialog.getButtonTypes()) {
      Node button = buttonNodes.computeIfAbsent(cmd, dialogButton -> createButton(cmd));

      // keep only first default button
      if (button instanceof Button) {
        ButtonBar.ButtonData buttonType = cmd.getButtonData();

        ((Button) button).setDefaultButton(
            !hasDefault && buttonType != null && buttonType.isDefaultButton()
        );
        ((Button) button).setCancelButton(buttonType != null && buttonType.isCancelButton());
        ((Button) button).setOnAction(evt -> {
          getSkinnable().getDialog().getResult().complete(cmd);
          getSkinnable().hideDialog();
        });

        hasDefault |= buttonType != null && buttonType.isDefaultButton();
      }
      dialogButtonBar.getButtons().add(button);
    }
  }

  protected Node createButton(ButtonType buttonType) {
    final Button button = new Button(buttonType.getText());
    final ButtonBar.ButtonData buttonData = buttonType.getButtonData();
    ButtonBar.setButtonData(button, buttonData);
    button.setDefaultButton(buttonData.isDefaultButton());
    button.setCancelButton(buttonData.isCancelButton());
    button.addEventHandler(ActionEvent.ACTION, ae -> {
      if (ae.isConsumed()) {
        return;
      }
//            if (dialog != null) {
//                dialog.setResultAndClose(buttonType, true);
//            }
    });

    return button;
  }

  @Override
  protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
    super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
    if (dialogPane.isVisible() && getChildren().contains(dialogPane)) {

      double dialogPrefWidth = dialogPane.prefWidth(-1);
      double dialogPrefHeight = dialogPane.prefHeight(-1);

      final WorkbenchDialog dialog = getSkinnable().getDialog();

      if (dialog == null) {
        dialogPrefWidth = dialogPane.getWidth();
        dialogPrefHeight = dialogPane.getHeight();
      } else if (dialog.isMaximize()) {
        dialogPrefWidth = contentWidth * .9;
        dialogPrefHeight = contentHeight * .9;
      }

      final double dialogTargetX = contentX + (contentWidth - dialogPrefWidth) / 2;
      final double dialogTargetY = contentY + (contentHeight - dialogPrefHeight) / 2;
      dialogPane.resizeRelocate(
          dialogTargetX, dialogTargetY, dialogPrefWidth, dialogPrefHeight);
    }

    final double gap = 30;
  }

}
