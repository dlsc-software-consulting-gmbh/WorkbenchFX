package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import java.util.concurrent.CompletableFuture;
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
      LogManager.getLogger(Workbench.class.getName());

  public enum Type {
    INPUT,
    INFORMATION,
    ERROR,
    WARNING,
    CONFIRMATION
  }

  private Type type;

  /**
   * Creates a new model object for a dialog.
   * @param type of the dialog
   */
  public WorkbenchDialog(Type type) {
    initType(type);
  }

  private void initType(Type type) {
    this.type = type;

    getStyleClass().add(type.name().toLowerCase());

    switch (type) {
      case INPUT:
        getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        break;
      case INFORMATION:
        getButtonTypes().setAll(ButtonType.OK);
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
      default:
        throw new UnsupportedOperationException("Dialog of this type doesn't exist!");
    }
  }

  /**
   * Creates a new model object for a dialog.
   * @param buttonTypes to be used in the dialog
   */
  public WorkbenchDialog(ButtonType... buttonTypes) {
    getButtonTypes().setAll(buttonTypes);
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
   * @param title       of the dialog
   * @param content     of the dialog
   * @param buttonTypes to be used in this dialog
   * @return builder object
   */
  public static WorkbenchDialogBuilder builder(
      String title, Node content, ButtonType... buttonTypes) {
    return new WorkbenchDialogBuilder(title, content, buttonTypes);
  }

  WorkbenchDialog(WorkbenchDialogBuilder workbenchDialogBuilder) {
    if (workbenchDialogBuilder.type != null) {
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
  }

  public final Type getType() {
    return type;
  }

  private final CompletableFuture<ButtonType> result = new CompletableFuture<>();

  public final CompletableFuture<ButtonType> getResult() {
    return result;
  }

  // button types

  private ObservableList<ButtonType> buttonTypes = FXCollections.observableArrayList();

  public ObservableList<ButtonType> getButtonTypes() {
    return buttonTypes;
  }

  // maximized

  private final BooleanProperty maximized = new SimpleBooleanProperty();

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

  private final ObjectProperty<Node> content = new SimpleObjectProperty<>(this, "content");

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

  private final StringProperty title = new SimpleStringProperty(this, "title", "Dialog");

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

  private final ObservableList<String> styleClass = FXCollections.observableArrayList();

  public ObservableList<String> getStyleClass() {
    return styleClass;
  }


  // Show buttons bar
  private final BooleanProperty buttonsBarShown =
      new SimpleBooleanProperty(this, "buttonsBarShown", true);

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

  private final ObjectProperty<Exception> exception = new SimpleObjectProperty<>(this, "exception");

  public final ObjectProperty<Exception> exceptionProperty() {
    return exception;
  }

  public final void setException(Exception ex) {
    this.exception.set(ex);
  }

  public final Exception getException() {
    return exception.get();
  }

  // blocking dialog or non-blocking dialog (modal or not modal)

  private final BooleanProperty blocking = new SimpleBooleanProperty(false, "blocking");

  public BooleanProperty blockingProperty() {
    return blocking;
  }

  public void setBlocking(boolean blocking) {
    this.blocking.set(blocking);
  }

  public boolean getBlocking() {
    return blocking.get();
  }
}
