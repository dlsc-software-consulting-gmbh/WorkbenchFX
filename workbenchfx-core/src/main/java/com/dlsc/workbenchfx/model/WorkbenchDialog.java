package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import com.dlsc.workbenchfx.view.controls.dialog.DialogMessageContent;
import com.google.common.base.Strings;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

  private final StringProperty title = new SimpleStringProperty(this, "title");
  private final StringProperty details = new SimpleStringProperty(this, "details");

  private final BooleanProperty maximized =
      new SimpleBooleanProperty(this, "maximized");
  private final BooleanProperty blocking =
      new SimpleBooleanProperty(this, "blocking");
  private final BooleanProperty buttonsBarShown =
      new SimpleBooleanProperty(this, "buttonsBarShown");

  private final ObjectProperty<Node> content =
      new SimpleObjectProperty<>(this, "content");
  private final ObjectProperty<Exception> exception =
      new SimpleObjectProperty<>(this, "exception");
  private final ObjectProperty<Consumer<ButtonType>> onResult =
      new SimpleObjectProperty<>(this, "onResult");
  private final ObjectProperty<DialogControl> dialogControl =
      new SimpleObjectProperty<>(this, "dialogControl");

  private ObservableList<ButtonType> buttonTypes = FXCollections.observableArrayList();
  private final ObservableList<String> styleClass = FXCollections.observableArrayList();

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

  /**
   * Retrieves the {@link Button} instance of the {@link DialogControl} which is of the
   * specified {@link ButtonType}.
   *
   * @param buttonType to retrieve from the {@link DialogControl}
   * @return the button or an empty {@link Optional}, if the {@link DialogControl} hasn't been
   *         initialized before
   */
  public final Optional<Button> getButton(ButtonType buttonType) {
    if (Objects.isNull(getDialogControl())) {
      return Optional.empty();
    } else {
      return getDialogControl().getButton(buttonType);
    }
  }

  // Event Handler

  /**
   * The dialog's action, which is invoked whenever the dialog has been fully initialized and is
   * being shown. Whenever the {@link DialogControl#dialogProperty()}, {@link
   * WorkbenchDialog#buttonTypes}, {@link DialogControl#buttonTextUppercaseProperty()} or {@link
   * DialogControl#workbenchProperty()} changes, the dialog will be rebuilt and upon completion, an
   * event will be fired.
   *
   * @return the property to represent the event, which is invoked whenever the dialog has been
   * fully initialized and is being shown.
   */
  public final ObjectProperty<EventHandler<Event>> onShownProperty() {
    return getDialogControl().onShownProperty();
  }

  public final void setOnShown(EventHandler<Event> value) {
    getDialogControl().setOnShown(value);
  }

  public final EventHandler<Event> getOnShown() {
    return getDialogControl().getOnShown();
  }

  /**
   * The dialog's action, which is invoked whenever the dialog has been hidden in the scene graph.
   * An event will be fired whenever {@link DialogControl#hide()} or
   * {@link Workbench#hideDialog(WorkbenchDialog)} has been called or the dialog has been closed by
   * clicking on its corresponding {@link GlassPane}.
   *
   * @return the property to represent the event, which is invoked whenever the dialog has been
   *         hidden in the scene graph.
   */
  public final ObjectProperty<EventHandler<Event>> onHiddenProperty() {
    return getDialogControl().onHiddenProperty();
  }

  public final void setOnHidden(EventHandler<Event> value) {
    getDialogControl().setOnHidden(value);
  }

  public final EventHandler<Event> getOnHidden() {
    return getDialogControl().getOnHidden();
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

  // action to be performed when a button on the DialogControl has been pressed

  public Consumer<ButtonType> getOnResult() {
    return onResult.get();
  }

  public ObjectProperty<Consumer<ButtonType>> onResultProperty() {
    return onResult;
  }

  /**
   * Defines the action to perform when a button of the dialog was pressed.
   * @param onResult action to be performed
   * @implNote If {@code onResult} is null, an empty consumer will be set instead, to avoid
   *           throwing {@link NullPointerException} upon calling.
   */
  public void setOnResult(Consumer<ButtonType> onResult) {
    if (Objects.isNull(onResult)) {
      // set empty consumer instead to avoid NPE
      onResult = buttonType -> {
      };
    }
    this.onResult.set(onResult);
  }

  // Dialog Control to be used

  public DialogControl getDialogControl() {
    return dialogControl.get();
  }

  /**
   * The root node of the dialog, the {@link DialogControl} contains all visual
   * elements shown in the dialog. As such, it is possible to completely adjust
   * the display of the dialog by modifying the existing dialog control or creating
   * a new one.
   */
  public ObjectProperty<DialogControl> dialogControlProperty() {
    return dialogControl;
  }

  public void setDialogControl(DialogControl dialogControl) {
    this.dialogControl.set(dialogControl);
  }
}
