package com.dlsc.workbenchfx.view.dialog;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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

  private ReadOnlyObjectProperty<WorkbenchDialog> dialog;

  private Label dialogTitle;
  private VBox dialogPane;
  private HBox dialogHeader;
  private Button dialogCloseButton;
  private StackPane dialogContentPane;
  private ButtonBar dialogButtonBar;
  private final Map<ButtonType, Node> buttonNodes = new WeakHashMap<>();

  /**
   * Creates a new {@link DialogSkin} object for a corresponding {@link DialogControl}.
   *
   * @param dialogControl the {@link DialogControl} for which this Skin is created
   */
  public DialogSkin(DialogControl dialogControl) {
    super(dialogControl);

    dialog = dialogControl.dialogProperty();

    initializeParts();
    layoutParts();
    setupBindings();
    setupEventHandlers();
    setupValueChangedListeners();

    updateDialog(getSkinnable().getDialog());
  }

  private void initializeParts() {
    dialogPane = new VBox();
    dialogPane.getStyleClass().add("dialog-pane");

    dialogHeader = new HBox();
    dialogHeader.getStyleClass().add("dialog-header");

    dialogTitle = new Label("Dialog");
    dialogTitle.getStyleClass().add("dialog-title");

    dialogCloseButton = new Button();
    dialogCloseButton.getStyleClass().addAll("dialog-close-button", "dialog-close-icon");

    dialogContentPane = new StackPane();
    dialogContentPane.getStyleClass().add("dialog-content-pane");

    dialogButtonBar = new ButtonBar();
  }

  private void layoutParts() {
    dialogPane.setFillWidth(true);
    dialogPane.getChildren().setAll(dialogHeader, dialogContentPane, dialogButtonBar);

    VBox.setVgrow(dialogContentPane, Priority.ALWAYS);

    dialogHeader.setAlignment(Pos.CENTER_LEFT);
    dialogHeader.getChildren().setAll(dialogTitle, dialogCloseButton);

    dialogTitle.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(dialogTitle, Priority.ALWAYS);
    VBox.setVgrow(dialogTitle, Priority.NEVER);

    HBox.setHgrow(dialogCloseButton, Priority.NEVER);

    VBox.setVgrow(dialogButtonBar, Priority.NEVER);

    getChildren().add(dialogPane);
  }

  private void setupBindings() {
    dialogButtonBar.managedProperty().bind(dialogButtonBar.visibleProperty());
  }

  private void setupEventHandlers() {
    dialogCloseButton.setOnAction(evt -> {
      getSkinnable().getDialog().getResult().complete(ButtonType.CANCEL);
      getSkinnable().hide();
    });
  }

  private void setupValueChangedListeners() {
    dialog.addListener((observable, oldDialog, newDialog) -> updateDialog(newDialog));
  }

  private void updateDialog(WorkbenchDialog workbenchDialog) {
    if (!Objects.isNull(workbenchDialog)) {
      // reset bindings
      dialogTitle.textProperty().unbind();

      dialogTitle.textProperty().bind(workbenchDialog.titleProperty());
      dialogContentPane.getChildren().setAll(workbenchDialog.getContent());
      dialogPane.getStyleClass().setAll("dialog-pane");
      dialogPane.getStyleClass().addAll(workbenchDialog.getStyleClass());

      updateButtons(workbenchDialog);
    }
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
          getSkinnable().hide();
        });

        hasDefault |= buttonType != null && buttonType.isDefaultButton();
      }
      dialogButtonBar.getButtons().add(button);
    }
  }

  private Node createButton(ButtonType buttonType) {
    final Button button = new Button(buttonType.getText().toUpperCase());
    final ButtonBar.ButtonData buttonData = buttonType.getButtonData();
    ButtonBar.setButtonData(button, buttonData);
    button.setDefaultButton(buttonData.isDefaultButton());
    button.setCancelButton(buttonData.isCancelButton());
    button.addEventHandler(ActionEvent.ACTION, ae -> {
      if (ae.isConsumed()) {
        return;
      }
    });
    return button;
  }

  @Override
  protected void layoutChildren(
      double contentX, double contentY, double contentWidth, double contentHeight) {
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
  }

}
