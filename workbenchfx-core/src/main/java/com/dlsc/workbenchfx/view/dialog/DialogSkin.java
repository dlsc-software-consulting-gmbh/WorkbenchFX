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

/**
 * Represents the Skin of the {@link DialogControl}.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DialogSkin extends SkinBase<DialogControl> {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogSkin.class.getName());
  private static final double MARGIN_PERCENT = .1;

  private ReadOnlyObjectProperty<WorkbenchDialog> dialog;

  private Label dialogTitle;
  private VBox dialogPane;
  private HBox dialogHeader;
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

    updateDialog(null, getSkinnable().getDialog());
  }

  private void initializeParts() {
    dialogPane = new VBox();
    dialogPane.getStyleClass().add("dialog-pane");

    dialogHeader = new HBox();
    dialogHeader.getStyleClass().add("dialog-header");

    dialogTitle = new Label("Dialog");
    dialogTitle.getStyleClass().add("dialog-title");

    dialogContentPane = new StackPane();
    dialogContentPane.getStyleClass().add("dialog-content-pane");

    dialogButtonBar = new ButtonBar();
  }

  private void layoutParts() {
    dialogPane.setFillWidth(true);
    dialogPane.getChildren().setAll(dialogHeader, dialogContentPane, dialogButtonBar);

    VBox.setVgrow(dialogContentPane, Priority.ALWAYS);

    dialogHeader.setAlignment(Pos.CENTER_LEFT);
    dialogHeader.getChildren().setAll(dialogTitle);

    dialogTitle.setMaxWidth(Double.MAX_VALUE);
    dialogTitle.setWrapText(true);
    HBox.setHgrow(dialogTitle, Priority.ALWAYS);
    VBox.setVgrow(dialogTitle, Priority.NEVER);

    VBox.setVgrow(dialogButtonBar, Priority.NEVER);

    getChildren().add(dialogPane);

    // makes sure that when clicking on transparent pixels outside of dialog, GlassPane will still
    // receive click events!
    getSkinnable().setPickOnBounds(false);
  }

  private void setupBindings() {
    dialogButtonBar.managedProperty().bind(dialogButtonBar.visibleProperty());
  }

  private void setupEventHandlers() {

  }

  private void setupValueChangedListeners() {
    dialog.addListener((observable, oldDialog, newDialog) -> updateDialog(oldDialog, newDialog));
  }

  private void updateDialog(WorkbenchDialog oldDialog, WorkbenchDialog newDialog) {
    if (!Objects.isNull(newDialog)) {
      // undo old dialog
      dialogTitle.textProperty().unbind();
      if (!Objects.isNull(oldDialog)) {
        dialogPane.getStyleClass().removeAll(oldDialog.getStyleClass());
      }

      // update to new dialog
      dialogTitle.textProperty().bind(newDialog.titleProperty());
      dialogContentPane.getChildren().setAll(newDialog.getContent());
      dialogPane.getStyleClass().setAll("dialog-pane");
      dialogPane.getStyleClass().addAll(newDialog.getStyleClass());

      updateButtons(newDialog);
    }
  }

  private void updateButtons(WorkbenchDialog dialog) {
    dialogButtonBar.getButtons().clear();
    dialogButtonBar.setVisible(dialog.isButtonsBarShown());

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

      final double maxWidth = contentWidth * (1 - MARGIN_PERCENT);
      final double maxHeight = contentHeight * (1 - MARGIN_PERCENT);

      // on a maximized dialog, set the size to the maximum, else use the dialog size
      if (dialog == null) {
        dialogPrefWidth = dialogPane.getWidth();
        dialogPrefHeight = dialogPane.getHeight();
      } else if (dialog.isMaximized()) {
        dialogPrefWidth = maxWidth;
        dialogPrefHeight = maxHeight;
      }

      // make sure dialog doesn't get bigger than the maximum size
      if (dialogPrefWidth > maxWidth) {
        dialogPrefWidth = maxWidth;
      }
      if (dialogPrefHeight > maxHeight) {
        dialogPrefHeight = maxHeight;
      }

      final double dialogTargetX = contentX + (contentWidth - dialogPrefWidth) / 2;
      final double dialogTargetY = contentY + (contentHeight - dialogPrefHeight) / 2;
      dialogPane.resizeRelocate(
          dialogTargetX, dialogTargetY, dialogPrefWidth, dialogPrefHeight);

      // make sure the content and title are never longer than the dialog (causing it not to wrap)
      dialogContentPane.setMaxWidth(dialogPrefWidth);
      dialogTitle.setMaxWidth(dialogPrefWidth);
    }
  }

}
