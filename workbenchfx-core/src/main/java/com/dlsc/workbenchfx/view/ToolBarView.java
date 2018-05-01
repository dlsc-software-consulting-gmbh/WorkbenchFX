package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
public class ToolBarView extends HBox implements View {
  private final WorkbenchFx model;
  private FontAwesomeIconView homeIconView;
  private FontAwesomeIconView menuIconView;
  Button homeBtn;
  Button menuBtn;
  private HBox tabBox;
  HBox toolBarControlBox;

  /**
   * Creates a new {@link ToolBarView} for the Workbench.
   *
   * @param model the {@link WorkbenchFx} as a model
   */
  public ToolBarView(WorkbenchFx model) {
    this.model = model;
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
    homeIconView.setId("homeIconView");
    homeBtn = new Button("", homeIconView);
    homeBtn.setId("homeButton");
    homeBtn.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);

    menuIconView = new FontAwesomeIconView(FontAwesomeIcon.BARS);
    menuIconView.setId("menuIconView");
    menuBtn = new Button("", menuIconView);
    menuBtn.setId("menuButton");

    tabBox = new HBox();
    tabBox.setId("tabBox");

    toolBarControlBox = new HBox();
    toolBarControlBox.setId("toolbar-control-box");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().addAll(homeBtn, tabBox, toolBarControlBox);
    setHgrow(tabBox, Priority.ALWAYS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

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
   * @param index of the tab to be removed
   */
  public void removeTab(int index) {
    tabBox.getChildren().remove(index);
  }

  /**
   * Adds a {@link Node} at the end of the {@code toolBarControlBox}.
   *
   * @param toolBarControl the {@link Node} to be added
   */
  public void addToolBarControl(Node toolBarControl) {
    toolBarControl.getStyleClass().add("toolbar-control");
    toolBarControlBox.getChildren().add(toolBarControl);
  }

  /**
   * Removes a {@link Node} at the specified index of the {@code toolBarControlBox}.
   *
   * @param index the index where the specified {@link Node} should be removed
   */
  public void removeToolBarControl(int index) {
    toolBarControlBox.getChildren().remove(index);
  }
}
