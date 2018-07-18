package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.view.controls.GlassPane;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Builder class to create a {@link WorkbenchDialog} object.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchDialogBuilder {
  private static final Logger LOGGER = LogManager.getLogger(WorkbenchDialogBuilder.class.getName());

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

  WorkbenchDialogBuilder(String title, Node content, ButtonType... buttonTypes) {
    this.title = title;
    this.content = content;
    this.buttonTypes = buttonTypes;
    this.type = null;
  }

  WorkbenchDialogBuilder(String title, Node content, WorkbenchDialog.Type type) {
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
   * TODO
   */
  public WorkbenchDialogBuilder onResult(Consumer<ButtonType> onResult) {
    this.onResult = onResult;
    return this;
  }

  /**
   * TODO
   */
  public WorkbenchDialogBuilder dialogControl(DialogControl dialogControl) {
    this.dialogControl = dialogControl;
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
