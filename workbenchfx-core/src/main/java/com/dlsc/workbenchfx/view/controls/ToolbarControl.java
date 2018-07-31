package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.Workbench;
import java.util.LinkedHashSet;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a toolbar, which displays all toolbar items stored in the representative Lists.
 * It consists of two areas to display the items: The left and the right toolbarControlBox.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarControl extends HBox {
  private static final Logger LOGGER = LogManager.getLogger(ToolbarControl.class.getName());

  private HBox toolbarControlLeftBox;
  private HBox toolbarControlRightBox;

  // The content of the two HBoxes listens to the two Lists and will be set on change.
  private final ListProperty<Node> toolbarControlsLeft = new SimpleListProperty<>(this,
      "toolbarControlsLeft", FXCollections.observableArrayList());
  private final SetProperty<Node> toolbarControlsRight = new SimpleSetProperty<>(this,
      "toolbarControlsRight",
      FXCollections.observableSet(new LinkedHashSet<>()));

  private final BooleanProperty empty = new SimpleBooleanProperty(true);

  /**
   * Creates an empty {@link ToolbarControl} object and fully initializes it.
   */
  public ToolbarControl() {
    initializeParts();
    layoutParts();
    setupListeners();
    setupBindings();
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

    ObservableSet<Node> toolbarItems = FXCollections.observableSet();
    toolbarControlLeftBox.getChildren().setAll(toolbarItems);
  }

  private void layoutParts() {
    getChildren().addAll(toolbarControlLeftBox, toolbarControlRightBox);
    HBox.setHgrow(toolbarControlLeftBox, Priority.ALWAYS);
  }

  private void setupListeners() {
    toolbarControlsLeft.addListener((InvalidationListener) change ->
        toolbarControlLeftBox.getChildren().setAll(toolbarControlsLeft));
    toolbarControlsRight.addListener((InvalidationListener) change ->
        toolbarControlRightBox.getChildren().setAll(toolbarControlsRight));
  }

  private void setupBindings() {
    empty.bind(Bindings.isEmpty(toolbarControlsLeft).and(Bindings.isEmpty(toolbarControlsRight)));
  }

  /**
   * Returns whether the toolbar is empty or not.
   * @return whether the toolbar is empty or not
   */
  public boolean isEmpty() {
    return empty.get();
  }

  /**
   * Returns whether the toolbar is empty or not.
   * @return whether the toolbar is empty or not
   */
  public ReadOnlyBooleanProperty emptyProperty() {
    return empty;
  }

  public ObservableList<Node> getToolbarControlsLeft() {
    return toolbarControlsLeft.get();
  }

  public ListProperty<Node> toolbarControlsLeftProperty() {
    return toolbarControlsLeft;
  }

  public void setToolbarControlsLeft(ObservableList<Node> toolbarControlsLeft) {
    this.toolbarControlsLeft.set(toolbarControlsLeft);
  }

  public ObservableSet<Node> getToolbarControlsRight() {
    return toolbarControlsRight.get();
  }

  public SetProperty<Node> toolbarControlsRightProperty() {
    return toolbarControlsRight;
  }

  public void setToolbarControlsRight(ObservableSet<Node> toolbarControlsRight) {
    this.toolbarControlsRight.set(toolbarControlsRight);
  }
}
