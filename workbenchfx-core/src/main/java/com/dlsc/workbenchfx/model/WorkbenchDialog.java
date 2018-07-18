package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import com.dlsc.workbenchfx.view.controls.dialog.DialogMessageContent;
import com.google.common.base.Strings;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the model class of a Dialog in {@link Workbench}.

 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class WorkbenchDialog {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchDialog.class.getName());

  private Type type;
  private ObservableList<ButtonType> buttonTypes = FXCollections.observableArrayList();
  private final BooleanProperty maximized = new SimpleBooleanProperty();
  private final ObjectProperty<Node> content = new SimpleObjectProperty<>(this, "content");
  private final StringProperty title = new SimpleStringProperty(this, "title", "Dialog");
  private final StringProperty details = new SimpleStringProperty(this, "details", "");
  private final ObservableList<String> styleClass = FXCollections.observableArrayList();
  private final BooleanProperty buttonsBarShown =
      new SimpleBooleanProperty(this, "buttonsBarShown", true);
  private final ObjectProperty<Exception> exception = new SimpleObjectProperty<>(this, "exception");
  private final BooleanProperty blocking = new SimpleBooleanProperty(false, "blocking");
  private final ObjectProperty<Consumer<ButtonType>> onResult = new SimpleObjectProperty<>(this, "onResult");
  private final ObjectProperty<DialogControl> dialogControl = new SimpleObjectProperty<>(this, "dialogControl");

  public enum Type {
    INPUT,
    INFORMATION,
    ERROR,
    WARNING,
    CONFIRMATION
  }

  /**
   * Creates a builder for {@link WorkbenchDialog}.
   *
   * @param title   of the dialog
   * @param content of the dialog
   * @param type    of the dialog
   * @return builder object
   */
  public static WorkbenchDialogBuilder builder(String title, Node content, Type type) {
    return new WorkbenchDialogBuilder(title, content, type);
  }

  /**
   * Creates a builder for {@link WorkbenchDialog}.
   *
   * @param title   of the dialog
   * @param message of the dialog
   * @param type    of the dialog
   * @return builder object
   */
  public static WorkbenchDialogBuilder builder(String title, String message, Type type) {
    return new WorkbenchDialogBuilder(title, new DialogMessageContent(message), type);
  }

  /**
   * Creates a builder for {@link WorkbenchDialog}.
   *
   * @param title       of the dialog
   * @param content     of the dialog
   * @param buttonTypes to be used in this dialog
   * @return builder object
   */
  public static WorkbenchDialogBuilder builder(
      String title, Node content, ButtonType... buttonTypes) {
    return new WorkbenchDialogBuilder(title, content, buttonTypes);
  }

  /**
   * Creates a builder for {@link WorkbenchDialog}.
   *
   * @param title       of the dialog
   * @param message     of the dialog
   * @param buttonTypes to be used in this dialog
   * @return builder object
   */
  public static WorkbenchDialogBuilder builder(
      String title, String message, ButtonType... buttonTypes) {
    return new WorkbenchDialogBuilder(title, new DialogMessageContent(message), buttonTypes);
  }

  WorkbenchDialog(WorkbenchDialogBuilder workbenchDialogBuilder) {
    // update details with stacktrace of exception, whenever exception is changed
    exceptionProperty().addListener((observable, oldException, newException) -> {
      if (!Objects.isNull(newException)) {
        StringWriter stringWriter = new StringWriter();
        newException.printStackTrace(new PrintWriter(stringWriter));
        setDetails(stringWriter.toString());
      }
    });

    if (Objects.isNull(workbenchDialogBuilder.buttonTypes)) {
      // Type was defined
      initType(workbenchDialogBuilder.type);
    } else {
      // ButtonTypes were specified
      getButtonTypes().setAll(workbenchDialogBuilder.buttonTypes);
    }
    setTitle(workbenchDialogBuilder.title);
    setContent(workbenchDialogBuilder.content);
    setMaximized(workbenchDialogBuilder.maximized);
    setBlocking(workbenchDialogBuilder.blocking);
    setButtonsBarShown(workbenchDialogBuilder.showButtonsBar);
    getStyleClass().addAll(workbenchDialogBuilder.styleClasses);
    setException(workbenchDialogBuilder.exception);
    setOnResult(workbenchDialogBuilder.onResult);
    // don't override details set by exception listener if no details were specified
    if (!Strings.isNullOrEmpty(workbenchDialogBuilder.details)) {
      setDetails(workbenchDialogBuilder.details);
    }
    setDialogControl(workbenchDialogBuilder.dialogControl);

    // initialize dialog control
    getDialogControl().setDialog(this);
  }

  private void initType(Type type) {
    this.type = type;

    if (!Objects.isNull(type)) {
      getStyleClass().add(type.name().toLowerCase());
    } else {
      // don't add any buttonTypes if type is null
      return;
    }

    switch (type) {
      case INPUT:
        getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        break;
      case ERROR:
        getButtonTypes().setAll(ButtonType.CLOSE);
        break;
      case WARNING:
        getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        break;
      case CONFIRMATION:
        getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        break;
      default: // INFORMATION
        getButtonTypes().setAll(ButtonType.OK);
    }
  }

  public final Type getType() {
    return type;
  }

  // button types

  public ObservableList<ButtonType> getButtonTypes() {
    return buttonTypes;
  }

  // maximized

  public final BooleanProperty maximizedProperty() {
    return maximized;
  }

  public final void setMaximized(boolean max) {
    maximized.set(max);
  }

  public final boolean isMaximized() {
    return maximized.get();
  }

  // content

  public final ObjectProperty<Node> contentProperty() {
    return content;
  }

  public void setContent(Node content) {
    this.content.set(content);
  }

  public Node getContent() {
    return content.get();
  }

  // title

  public final StringProperty titleProperty() {
    return title;
  }

  public final String getTitle() {
    return title.get();
  }

  public final void setTitle(String title) {
    this.title.set(title);
  }

  // custom style

  public ObservableList<String> getStyleClass() {
    return styleClass;
  }

  // Show buttons bar

  public final BooleanProperty buttonsBarShownProperty() {
    return buttonsBarShown;
  }

  public final boolean isButtonsBarShown() {
    return buttonsBarShownProperty().get();
  }

  public final void setButtonsBarShown(boolean buttonsBarShown) {
    buttonsBarShownProperty().set(buttonsBarShown);
  }

  // exception

  public final ObjectProperty<Exception> exceptionProperty() {
    return exception;
  }

  public final void setException(Exception ex) {
    this.exception.set(ex);
  }

  public final Exception getException() {
    return exception.get();
  }

  // details

  public String getDetails() {
    return details.get();
  }

  public StringProperty detailsProperty() {
    return details;
  }

  public void setDetails(String details) {
    this.details.set(details);
  }


  // blocking dialog or non-blocking dialog (modal or not modal)

  public BooleanProperty blockingProperty() {
    return blocking;
  }

  public void setBlocking(boolean blocking) {
    this.blocking.set(blocking);
  }

  public boolean isBlocking() {
    return blocking.get();
  }

  // action to be performed when dialog has been completed // TODO

  public Consumer<ButtonType> getOnResult() {
    return onResult.get();
  }

  public ObjectProperty<Consumer<ButtonType>> onResultProperty() {
    return onResult;
  }

  public void setOnResult(Consumer<ButtonType> onResult) {
    if (Objects.isNull(onResult)) {
      // set empty consumer instead to avoid NPE
      onResult = buttonType -> {};
    }
    this.onResult.set(onResult);
  }

  // Dialog Control to be used

  public DialogControl getDialogControl() {
    return dialogControl.get();
  }

  public ObjectProperty<DialogControl> dialogControlProperty() {
    return dialogControl;
  }

  public void setDialogControl(DialogControl dialogControl) {
    this.dialogControl.set(dialogControl);
  }
}
