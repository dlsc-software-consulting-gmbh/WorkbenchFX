package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View {
  private final WorkbenchFx model;
  private DropdownView dropdownView;
  private FontAwesomeIconView homeIconView;
  Button homeBtn;
  private HBox tabBox;

  // TODO: add javadoc comment
  public ToolBarView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  public ToolBarView(WorkbenchFx model, DropdownView dropdownView) {
    this.model = model;
    this.dropdownView = dropdownView;
    init();
    setMaxHeight(10);
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

    tabBox = new HBox();
    tabBox.setId("tabBox");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().addAll(homeBtn, tabBox, dropdownView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

  // TODO: add javadoc comment
  public void addTab(Node tab) {
    tabBox.getChildren().add(tab);
  }

  // TODO: add javadoc comment
  public void removeTab(int index) {
    tabBox.getChildren().remove(index);
  }

}
