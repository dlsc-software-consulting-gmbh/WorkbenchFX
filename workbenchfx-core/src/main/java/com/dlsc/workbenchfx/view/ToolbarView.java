package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_HOME;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.selectionstrip.SelectionStrip;
import javafx.application.Platform;
import javafx.scene.Node;
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
public class ToolbarView extends VBox implements View {

  private HBox topBox;
  private HBox bottomBox;

  private StackPane addIconShape;
  Button addBtn;
  private StackPane menuIconShape;
  Button menuBtn;
  SelectionStrip<WorkbenchModule> tabBar;
  HBox toolbarControlLeftBox;
  HBox toolbarControlRightBox;

  /**
   * Creates a new {@link ToolbarView} for the Workbench.
   */
  public ToolbarView() {
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("toolbar");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    topBox = new HBox();
    topBox.setId("top-box");
    bottomBox = new HBox();
    bottomBox.setId("bottom-box");

    addIconShape = new StackPane();
    addIconShape.getStyleClass().add("shape");
    addBtn = new Button("", addIconShape);
    addBtn.getStyleClass().add("icon");
    addBtn.setId("add-button");
    addBtn.getStyleClass().add(STYLE_CLASS_ACTIVE_HOME);

    menuIconShape = new StackPane();
    menuIconShape.getStyleClass().add("shape");
    menuBtn = new Button("", menuIconShape);
    menuBtn.getStyleClass().add("icon");
    menuBtn.setId("menu-button");

    tabBar = new SelectionStrip<>();
    // Reset default sizing from the selectionStrip constructor
    tabBar.setPrefSize(0, 0);
    tabBar.setId("tab-bar");

    toolbarControlLeftBox = new HBox();
    toolbarControlLeftBox.setId("toolbar-control-left-box");

    toolbarControlRightBox = new HBox();
    toolbarControlRightBox.setId("toolbar-control-right-box");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    topBox.getChildren().addAll(
        toolbarControlLeftBox,
        toolbarControlRightBox
    );
    HBox.setHgrow(toolbarControlLeftBox, Priority.ALWAYS);

    bottomBox.getChildren().addAll(
        tabBar,
        addBtn
    );
    HBox.setHgrow(tabBar, Priority.ALWAYS);

    getChildren().addAll(
        topBox,
        bottomBox
    );
    Platform.runLater(() -> addBtn.requestFocus());
  }

  /**
   * Shows a menu button in the front of the toolbar.
   */
  public void addMenuButton() {
    if (!getChildren().contains(menuBtn)) {
      topBox.getChildren().add(0, menuBtn);
    }
  }

  /**
   * Removes the menu button from the toolbar.
   */
  public void removeMenuButton() {
    getChildren().remove(menuBtn);
  }

  /**
   * Adds a {@link Node} at the end of the {@code toolbarControlLeftBox}.
   *
   * @param toolbarControlLeft the {@link Node} to be added
   */
  public void addToolbarControlLeft(Node toolbarControlLeft) {
    toolbarControlLeftBox.getChildren().add(toolbarControlLeft);
  }

  /**
   * Removes the {@code control} from the left toolbar.
   *
   * @param control the control to be removed
   */
  public void removeToolbarControlLeft(Node control) {
    toolbarControlLeftBox.getChildren().remove(control);
  }

  /**
   * Adds a {@link Node} at the end of the {@code toolbarControlRightBox}.
   *
   * @param toolbarControlRight the {@link Node} to be added
   */
  public void addToolbarControlRight(Node toolbarControlRight) {
    toolbarControlRightBox.getChildren().add(toolbarControlRight);
  }

  /**
   * Removes the {@code control} from the right toolbar.
   *
   * @param control the control to be removed
   */
  public void removeToolbarControlRight(Node control) {
    toolbarControlRightBox.getChildren().remove(control);
  }
}
