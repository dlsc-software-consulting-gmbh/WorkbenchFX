package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display dialogs in the {@link Workbench}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DialogControl extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogControl.class.getName());

  private final ObjectProperty<Workbench> workbench = new SimpleObjectProperty<>();
  private final ObjectProperty<WorkbenchDialog> dialog = new SimpleObjectProperty<>();
  private final BooleanProperty buttonTextUppercase = new SimpleBooleanProperty(false);
  private final ObservableList<Node> buttons = FXCollections.observableArrayList();
  private final Map<ButtonType, Node> buttonNodes = new WeakHashMap<>();
  private InvalidationListener dialogChangedListener;

  /**
   * Creates a dialog control.
   */
  public DialogControl() {
    // makes sure that when clicking on transparent pixels outside of dialog, GlassPane will still
    // receive click events!
    setPickOnBounds(false);

    setupChangeListeners();

    bindDialog();
  }

  private void setupChangeListeners() {
    // update tiles list whenever modules or the pageIndex of this page have changed
    dialogChangedListener = observable -> updateButtons(getDialog());
    dialog.addListener((observable, oldDialog, newDialog) -> {
      updateButtons(newDialog);
      if (!Objects.isNull(oldDialog)) {
        oldDialog.getButtonTypes().removeListener(dialogChangedListener);
      }
      if (!Objects.isNull(newDialog)) {
        newDialog.getButtonTypes().addListener(dialogChangedListener);
      }
    });
    workbench.addListener(dialogChangedListener);
    buttonTextUppercase.addListener(observable -> {
      buttonNodes.clear(); // force re-creation of buttons
      updateButtons(getDialog());
    });
  }

  private void updateButtons(WorkbenchDialog dialog) {
    LOGGER.trace("Updating buttons");

    buttons.clear();

    if (Objects.isNull(dialog)) {
      return;
    }

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
          getDialog().getResult().complete(cmd);
          hide();
        });

        hasDefault |= buttonType != null && buttonType.isDefaultButton();
      }
      buttons.add(button);
    }
  }

  private Node createButton(ButtonType buttonType) {
    LOGGER.trace("Create Button: " + buttonType.getText());
    String buttonText;
    if (isButtonTextUppercase()) {
      buttonText = buttonType.getText().toUpperCase();
    } else {
      buttonText = buttonType.getText();
    }
    final Button button = new Button(buttonText);
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

  /**
   * Binds {@link Workbench#dialogProperty()} of the workbench to this {@code dialog} property,
   * so the skin doesn't have to account for a potentially changing workbench.
   */
  private void bindDialog() {
    // add listener for changing workbench
    workbench.addListener((observable, oldWorkbench, newWorkbench) -> {
      dialog.unbind();
      dialog.set(null);
      if (!Objects.isNull(newWorkbench)) {
        dialog.bind(newWorkbench.dialogProperty());
      }
    });
  }

  public final void hide() {
    getWorkbench().hideDialog();
  }

  public WorkbenchDialog getDialog() {
    return dialog.get();
  }

  public ReadOnlyObjectProperty<WorkbenchDialog> dialogProperty() {
    return dialog;
  }

  public ObservableList<Node> getButtons() {
    return FXCollections.unmodifiableObservableList(buttons);
  }

  public boolean isButtonTextUppercase() {
    return buttonTextUppercase.get();
  }

  public BooleanProperty buttonTextUppercaseProperty() {
    return buttonTextUppercase;
  }

  public void setButtonTextUppercase(boolean buttonTextUppercase) {
    this.buttonTextUppercase.set(buttonTextUppercase);
  }

  private Workbench getWorkbench() {
    return workbench.get();
  }

  public final void setWorkbench(Workbench workbench) {
    this.workbench.set(workbench);
  }

  private ObjectProperty<Workbench> workbenchProperty() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DialogSkin(this);
  }
}
