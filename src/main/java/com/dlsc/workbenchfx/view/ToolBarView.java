package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View {
  private final WorkbenchFx model;
  Button homeBtn;
  HBox tabBox;

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

  FontAwesomeIconView fav;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
//    FontAwesomeIcon hIcon = FontAwesomeIcon.HOME;
    fav = new FontAwesomeIconView();
//    fav.setGlyphSize(100);
    homeBtn = new Button("", new FontAwesomeIconView(FontAwesomeIcon.HOME));
    fav.setStyleClass("glyph-icon");
    homeBtn.setId("homeButton");

    tabBox = new HBox();
    tabBox.setId("tabBox");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().addAll(fav, homeBtn, tabBox);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

  public void addTab(Node tab) {
    tabBox.getChildren().add(tab);
  }

  public void removeTab(int index) {
    tabBox.getChildren().remove(index);
  }

}
