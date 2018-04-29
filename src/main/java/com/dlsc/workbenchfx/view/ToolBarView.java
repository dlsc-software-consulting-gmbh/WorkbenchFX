package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * TODO
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

  // TODO: add javadoc comment
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().addAll(homeBtn, tabBox);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

  /**
   * TODO
   */
  public void addMenuButton() {
    if (!getChildren().contains(menuBtn)) {
      getChildren().add(0, menuBtn);
    }
  }

  /**
   * TODO
   */
  public void removeMenuButton() {
    getChildren().remove(menuBtn);
  }

  /**
   * TODO
   * @param tab
   */
  public void addTab(Node tab) {
    tabBox.getChildren().add(tab);
  }

  /**
   * TODO
   * @param index
   */
  public void removeTab(int index) {
    tabBox.getChildren().remove(index);
  }

}
