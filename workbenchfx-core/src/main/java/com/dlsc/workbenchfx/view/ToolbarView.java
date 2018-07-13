package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_HOME;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import com.dlsc.workbenchfx.view.controls.selectionstrip.SelectionStrip;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

  private FontAwesomeIconView homeIconView;
  private FontAwesomeIconView menuIconView;
  Button homeBtn;
  Button menuBtn;
  SelectionStrip<WorkbenchModule> tabBar;
  HBox toolbarControlLeftBox;
  HBox toolbarControlRightBox;

  /**
   * Creates a new {@link ToolbarView} for the Workbench.
   *
   * @param tabBar the Control which is used to create and display the {@link Tab}s
   */
  public ToolbarView(SelectionStrip<WorkbenchModule> tabBar) {
    this.tabBar = tabBar;
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

    homeIconView = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
    homeIconView.setId("home-icon-view");
    homeIconView.getStyleClass().add("icon-view");
    homeBtn = new Button("", homeIconView);
    homeBtn.setId("home-button");
    homeBtn.getStyleClass().add(STYLE_CLASS_ACTIVE_HOME);

    menuIconView = new FontAwesomeIconView(FontAwesomeIcon.BARS);
    menuIconView.setId("menu-icon-view");
    menuIconView.getStyleClass().add("icon-view");
    menuBtn = new Button("", menuIconView);
    menuBtn.setId("menu-button");

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
        homeBtn
    );
    HBox.setHgrow(tabBar, Priority.ALWAYS);

    getChildren().addAll(
        topBox,
        bottomBox
    );
    Platform.runLater(() -> homeBtn.requestFocus());
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
