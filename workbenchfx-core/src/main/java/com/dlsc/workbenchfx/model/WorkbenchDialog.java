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
import javafx.beans.property.ReadOnlyBooleanProperty;
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
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the model class of a Dialog in {@link Workbench}.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class WorkbenchDialog {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchDialog.class.getName());

  private static final ButtonType CANCEL_DIALOG_BUTTON_TYPE = ButtonType.CANCEL;

  private Type type;
  private ButtonType cancelDialogButtonType;

  private final StringProperty title = new SimpleStringProperty(this, "title");
  private final StringProperty details = new SimpleStringProperty(this, "details", "");

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

  // Builder
  public static class WorkbenchDialogBuilder {
    private static final Logger LOGGER =
        LogManager.getLogger(WorkbenchDialogBuilder.class.getName());

    // Required parameters - only either type or buttonTypes are required
    final WorkbenchDialog.Type type;
    final ButtonType[] buttonTypes;
    final String title;
    final Node content;

    // Optional parameters - initialized to default values
    boolean blocking = false;
    boolean maximized = false;
    boolean showButtonsBar = true;
    String[] styleClasses = new String[0];
    Exception exception = null;
    String details = "";
    Consumer<ButtonType> onResult = null;
    DialogControl dialogControl = new DialogControl();
    EventHandler<Event> onShown = null;
    EventHandler<Event> onHidden = null;

    private WorkbenchDialogBuilder(String title, Node content, ButtonType... buttonTypes) {
      this.title = title;
      this.content = content;
      this.buttonTypes = buttonTypes;
      this.type = null;
    }

    private WorkbenchDialogBuilder(String title, Node content, WorkbenchDialog.Type type) {
      this.title = title;
      this.content = content;
      this.type = type;
      this.buttonTypes = null;
    }

    /**
     * Defines whether the dialog is blocking (modal) or not.
     *
     * @param blocking If false (non-blocking), clicking outside of the {@code dialog} will cause it
     *                 to get hidden, together with its {@link GlassPane}. If true (blocking),
     *                 clicking outside of the {@code dialog} will not do anything. In this case,
     *                 the {@code dialog} must be closed by pressing one of the buttons.
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder blocking(boolean blocking) {
      this.blocking = blocking;
      return this;
    }

    /**
     * Defines whether the dialog is maximized or not.
     *
     * @param maximized whether or not the dialog should be scaled to fit the whole window
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder maximized(boolean maximized) {
      this.maximized = maximized;
      return this;
    }

    /**
     * Defines whether the buttons on the dialog should be shown or not.
     *
     * @param showButtonsBar if true, will show buttons, if false, will hide them
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder showButtonsBar(boolean showButtonsBar) {
      this.showButtonsBar = showButtonsBar;
      return this;
    }

    /**
     * Defines the style classes to set on the dialog.
     *
     * @param styleClasses to be set on the dialog
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder styleClass(String... styleClasses) {
      this.styleClasses = styleClasses;
      return this;
    }

    /**
     * Defines the details of an error to be shown in an <b>error</b> dialog.
     *
     * @param details to be shown
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder details(String details) {
      this.details = details;
      return this;
    }

    /**
     * Defines the exception to be shown in an <b>error</b> dialog and
     * sets {@link WorkbenchDialog#details} to the stacktrace of this {@code exception}.
     *
     * @param exception to be shown
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder exception(Exception exception) {
      this.exception = exception;
      return this;
    }

    /**
     * Defines the action to perform when a button of the dialog was pressed.
     *
     * @param onResult action to be performed
     * @return builder for chaining
     * @implNote If {@code onResult} is null, an empty consumer will be set instead, to avoid
     *           throwing {@link NullPointerException} upon calling.
     */
    public WorkbenchDialogBuilder onResult(Consumer<ButtonType> onResult) {
      this.onResult = onResult;
      return this;
    }

    /**
     * Defines which {@link DialogControl} should be used to render the {@link WorkbenchDialog}.
     *
     * @param dialogControl to be used to render the {@link WorkbenchDialog}.
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder dialogControl(DialogControl dialogControl) {
      this.dialogControl = dialogControl;
      return this;
    }

    /**
     * The dialog's action, which is invoked whenever the dialog has been fully initialized and is
     * being shown. Whenever the {@link DialogControl#dialogProperty()}, {@link
     * WorkbenchDialog#buttonTypes}, {@link DialogControl#buttonTextUppercaseProperty()} or {@link
     * DialogControl#workbenchProperty()} changes, the dialog will be rebuilt and upon completion,
     * an event will be fired.
     *
     * @param onShown action to be performed
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder onShown(EventHandler<Event> onShown) {
      this.onShown = onShown;
      return this;
    }

    /**
     * The dialog's action, which is invoked whenever the dialog has been hidden in the scene graph.
     * An event will be fired whenever {@link DialogControl#hide()} or
     * {@link Workbench#hideDialog(WorkbenchDialog)} has been called or the dialog has been closed
     * by clicking on its corresponding {@link GlassPane}.
     *
     * @param onHidden action to be performed
     * @return builder for chaining
     */
    public WorkbenchDialogBuilder onHidden(EventHandler<Event> onHidden) {
      this.onHidden = onHidden;
      return this;
    }

    /**
     * Builds and fully initializes a {@link WorkbenchDialog} object.
     *
     * @return the {@link WorkbenchDialog} object
     */
    public WorkbenchDialog build() {
      return new WorkbenchDialog(this);
    }
  }

  private WorkbenchDialog(WorkbenchDialogBuilder builder) {
    // update details with stacktrace of exception, whenever exception is changed
    exceptionProperty().addListener((observable, oldException, newException) -> {
      if (!Objects.isNull(newException)) {
        StringWriter stringWriter = new StringWriter();
        newException.printStackTrace(new PrintWriter(stringWriter));
        setDetails(stringWriter.toString());
      }
    });

    if (Objects.isNull(builder.buttonTypes)) {
      // Type was defined
      initType(builder.type);
    } else {
      // ButtonTypes were specified
      getButtonTypes().setAll(builder.buttonTypes);
    }
    setTitle(builder.title);
    setContent(builder.content);
    setMaximized(builder.maximized);
    setBlocking(builder.blocking);
    setButtonsBarShown(builder.showButtonsBar);
    getStyleClass().addAll(builder.styleClasses);
    setException(builder.exception);
    setOnResult(builder.onResult);
    // don't override details set by exception listener if no details were specified
    if (!Strings.isNullOrEmpty(builder.details)) {
      setDetails(builder.details);
    }
    setDialogControl(builder.dialogControl);
    if (!Objects.isNull(getDialogControl())) {
      setOnShown(builder.onShown);
      setOnHidden(builder.onHidden);
      // initialize dialog control
      getDialogControl().setDialog(this);
    }
    // set itself to changing dialogControls
    dialogControlProperty().addListener((observable, oldDialogControl, newDialogControl) -> {
      newDialogControl.setDialog(this);
    });
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
   *         fully initialized and is being shown.
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
   * An event will be fired whenever {@link DialogControl#hide()} or {@link
   * Workbench#hideDialog(WorkbenchDialog)} has been called or the dialog has been closed by
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
   * The resulting {@link ButtonType} usually reflects the {@link Button} which was pressed in the
   * dialog.<br>
   * If the dialog is non-blocking and has been closed by pressing on the {@link GlassPane} or
   * {@link KeyCode#ESCAPE}, the result will be the {@link ButtonType} of
   * {@link Button#isCancelButton()} in the dialog.<br>
   * If there is no {@link Button#isCancelButton()}, the result will be {@link ButtonType#CANCEL}.
   *
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
   * The root node of the dialog, the {@link DialogControl} contains all visual elements shown in
   * the dialog. As such, it is possible to completely adjust the display of the dialog by modifying
   * the existing dialog control or creating a new one.
   * @return the {@code dialogControl} of this {@link WorkbenchDialog}
   */
  public ObjectProperty<DialogControl> dialogControlProperty() {
    return dialogControl;
  }

  public void setDialogControl(DialogControl dialogControl) {
    this.dialogControl.set(dialogControl);
  }

  // Is this dialog showing or not?

  /**
   * Represents whether the dialog is currently showing.
   * @return the property representing whether the dialog is currently showing
   */
  public final ReadOnlyBooleanProperty showingProperty() {
    return dialogControl.get().showingProperty();
  }

  /**
   * Returns whether or not the dialog is showing.
   *
   * @return true if dialog is showing.
   */
  public final boolean isShowing() {
    return showingProperty().get();
  }
}
