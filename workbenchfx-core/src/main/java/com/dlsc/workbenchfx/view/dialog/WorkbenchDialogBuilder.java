package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.WorkbenchBuilder;
import com.dlsc.workbenchfx.view.controls.GlassPane;
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
   * @param blocking If false (non-blocking), clicking outside of the {@code dialog} will cause it
   *                 to get hidden, together with its {@link GlassPane}. If true (blocking),
   *                 clicking outside of the {@code dialog} will not do anything. In this case,
   *                 the {@code dialog} must be closed by pressing one of the buttons.
   * @return builder for chaining
   */
  public WorkbenchDialogBuilder setBlocking(boolean blocking) {
    this.blocking = blocking;
    return this;
  }

  /**
   * Defines whether the dialog is maximized or not.
   * @param maximized whether or not the dialog should be scaled to fit the whole window
   * @return builder for chaining
   */
  public WorkbenchDialogBuilder setMaximized(boolean maximized) {
    this.maximized = maximized;
    return this;
  }

  /**
   * Builds and fully initializes a {@link WorkbenchDialog} object.
   *
   * @return the {@link WorkbenchDialog} object
   */
  public WorkbenchDialog<ButtonType> build() {
    return new WorkbenchDialog<>(this);
  }
}
