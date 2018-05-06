package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_HOME;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Represents the toolbar which is being shown at the top of the window.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarView extends HBox implements View {
  private FontAwesomeIconView homeIconView;
  private FontAwesomeIconView menuIconView;
  Button homeBtn;
  Button menuBtn;
  private HBox tabBox;
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
    homeIconView = new FontAwesomeIconView(FontAwesomeIcon.HOME);
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

    tabBox = new HBox();
    tabBox.setId("tab-box");

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
    getChildren().addAll(toolbarControlLeftBox, homeBtn, tabBox, toolbarControlRightBox);
    setHgrow(tabBox, Priority.ALWAYS);
    Platform.runLater(() -> homeBtn.requestFocus());
  }

  /**
   * Shows a menu button in the front of the toolbar.
   */
  public void addMenuButton() {
    if (!getChildren().contains(menuBtn)) {
      getChildren().add(0, menuBtn);
    }
  }

  /**
   * Removes the menu button from the toolbar.
   */
  public void removeMenuButton() {
    getChildren().remove(menuBtn);
  }

  /**
   * Adds a tab to the {@code tabBox}.
   * @param tab to be added
   */
  /**
   * Adds a {@link Node} at the end of the {@code tabBox}.
   *
   * @param tab the {@link Node} to be added
   */
  public void addTab(Node tab) {
    tabBox.getChildren().add(tab);
  }

  /**
   * Removes a {@link Node} at the specified index of the {@code tabBox}.
   *
   * @param index the index where the specified {@link Node} should be removed
   */
  /**
   * Removes a tab to the {@code tabBox}.
   *
   * @param index of the tab to be removed
   */
  public void removeTab(int index) {
    tabBox.getChildren().remove(index);
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
   * Removes a {@link Node} at the specified index of the {@code toolbarControlLeftBox}.
   *
   * @param index the index where the specified {@link Node} should be removed
   */
  public void removeToolbarControlLeft(int index) {
    toolbarControlLeftBox.getChildren().remove(index);
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
   * Removes a {@link Node} at the specified index of the {@code toolbarControlRightBox}.
   *
   * @param index the index where the specified {@link Node} should be removed
   */
  public void removeToolbarControlRight(int index) {
    toolbarControlRightBox.getChildren().remove(index);
  }
}
