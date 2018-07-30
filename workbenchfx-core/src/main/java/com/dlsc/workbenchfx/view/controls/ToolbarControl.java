package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Represents a toolbar, which displays all toolbar items of a {@link
 * com.dlsc.workbenchfx.model.WorkbenchModule}. It consists of two areas to display the items: The
 * left and the right toolbarControlBox.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarControl extends HBox {

  private HBox toolbarControlLeftBox;
  private HBox toolbarControlRightBox;
  private WorkbenchModule workbenchModule;

  // The content of the two HBoxes listens to the two Sets and will be set on change.
  private final SetProperty<Node> toolbarControlsLeft = new SimpleSetProperty<>(this,
      "toolbarControlsLeft",
      FXCollections.observableSet());
  private final SetProperty<Node> toolbarControlsRight = new SimpleSetProperty<>(this,
      "toolbarControlsRight",
      FXCollections.observableSet());

  /**
   * Creates an empty {@link ToolbarControl} object and fully initializes it.
   */
  public ToolbarControl() {
    initializeParts();
    layoutParts();
    setupListeners();
  }

  private void initializeParts() {
    getStylesheets().add(
        Workbench.class.getResource("css/toolbar-control.css").toExternalForm()
    );

    getStyleClass().add("toolbar-control");

    toolbarControlLeftBox = new HBox();
    toolbarControlLeftBox.getStyleClass().add("toolbar-control-left-box");

    toolbarControlRightBox = new HBox();
    toolbarControlRightBox.getStyleClass().add("toolbar-control-right-box");

    ObservableSet<Node> objects = FXCollections.observableSet();
    toolbarControlLeftBox.getChildren().setAll(objects);
  }

  private void layoutParts() {
    getChildren().addAll(toolbarControlLeftBox, toolbarControlRightBox);
    HBox.setHgrow(toolbarControlLeftBox, Priority.ALWAYS);
  }

  private void setupListeners() {
    toolbarControlsLeft.addListener((InvalidationListener) c ->
        toolbarControlLeftBox.getChildren().setAll(toolbarControlsLeft));
    toolbarControlsRight.addListener((InvalidationListener) c ->
        toolbarControlRightBox.getChildren().setAll(toolbarControlsRight));
  }

  /**
   * Sets a {@link WorkbenchModule} and binds this Controls Sets to the Modules toolbar items.
   * @param workbenchModule whose toolbar items will be displayed in the Control
   */
  public void setModule(WorkbenchModule workbenchModule) {
    this.workbenchModule = workbenchModule;

    // Unbind Modules, which were set before
    toolbarControlsLeft.unbind();
    toolbarControlsRight.unbind();

    // Bind new Module
    toolbarControlsLeft.bindContent(workbenchModule.getToolbarControlsLeft());
    toolbarControlsRight.bindContent(workbenchModule.getToolbarControlsRight());
  }

  /**
   * Returns whether the {@link ToolbarControl} is empty or not.
   *
   * @return whether the {@link ToolbarControl} is empty or not
   */
  public boolean isEmpty() {
    return toolbarControlLeftBox.getChildren().isEmpty()
        && toolbarControlRightBox.getChildren().isEmpty();
  }
}
