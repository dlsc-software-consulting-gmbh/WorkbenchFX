package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarControl;
import com.dlsc.workbenchfx.view.controls.selectionstrip.SelectionStrip;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Represents the toolbar which is being shown at the top of the window.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class ToolbarView extends VBox implements View {

  HBox topBox;
  ToolbarControl toolbarControl;
  HBox bottomBox;

  StackPane addIconShape;
  Button addModuleBtn;
  StackPane menuIconShape;
  Button menuBtn;
  SelectionStrip<WorkbenchModule> tabBar;
  boolean showTabBar;

  /**
   * Creates a new {@link ToolbarView} for the Workbench.
   * @param showTabBar indicates whether the tab bar and that add module button should be visible
   */
  public ToolbarView(boolean showTabBar){
    this.showTabBar = showTabBar;
    init();
  }

  /**
   * Creates a new {@link ToolbarView} for the Workbench with a visible tab bar.
   */
  public ToolbarView() {
    this(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeSelf() {
    setId("toolbar");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeParts() {
    topBox = new HBox();
    topBox.setId("top-box");

    toolbarControl = new ToolbarControl();
    toolbarControl.setId("toolbar-control");

    bottomBox = new HBox();
    bottomBox.setId("bottom-box");

    addIconShape = new StackPane();
    addIconShape.getStyleClass().add("shape");
    addModuleBtn = new Button("", addIconShape);
    addModuleBtn.getStyleClass().add("icon");
    addModuleBtn.setId("add-button");

    menuIconShape = new StackPane();
    menuIconShape.getStyleClass().add("shape");
    menuBtn = new Button("", menuIconShape);
    menuBtn.getStyleClass().add("icon");
    menuBtn.setId("menu-button");

    tabBar = new SelectionStrip<>();
    // Reset default sizing from the selectionStrip constructor
    tabBar.setPrefSize(0, 0);
    tabBar.setId("tab-bar");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void layoutParts() {
    topBox.getChildren().add(toolbarControl);
    HBox.setHgrow(toolbarControl, Priority.ALWAYS);
    getChildren().add(topBox);

    if (showTabBar){
      bottomBox.getChildren().addAll(tabBar, addModuleBtn);
      HBox.setHgrow(tabBar, Priority.ALWAYS);
      getChildren().add(bottomBox);
      Platform.runLater(() -> addModuleBtn.requestFocus());
    }
  }

  /**
   * Removes the menuBtn wherever it is located.
   */
  final void removeMenuBtn() {
    bottomBox.getChildren().remove(menuBtn);
    topBox.getChildren().remove(menuBtn);
  }

  /**
   * Adds the menuBtn in first position of the topBox.
   */
  final void addMenuBtnTop() {
    topBox.getChildren().add(0, menuBtn);
  }

  /**
   * Adds the menuBtn in first position of the bottomBox.
   */
  final void addMenuBtnBottom() {
    bottomBox.getChildren().add(0, menuBtn);
  }
}
