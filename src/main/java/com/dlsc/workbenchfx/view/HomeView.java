package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.module.Module;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class HomeView extends StackPane implements View {
  private final WorkbenchFx model;
  HBox tileBox;

  /**
   * Creates a new {@link HomeView}.
   */
  public HomeView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("home");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    tileBox = new HBox();
    tileBox.setId("tileBox");

    int pageCount = model.getModules().size() / model.MODULES_PER_PAGE + 1;
    Pagination pagination = new Pagination(pageCount);
    pagination.setPageFactory(this::createPage);
    pagination.setMaxPageIndicatorCount(100);
    pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

    AnchorPane anchor = new AnchorPane();
    AnchorPane.setTopAnchor(pagination, 0.0);
    AnchorPane.setRightAnchor(pagination, 10.0);
    AnchorPane.setBottomAnchor(pagination, 60.0);
    AnchorPane.setLeftAnchor(pagination, 10.0);
    anchor.getChildren().addAll(pagination);
  }

  private Node createPage(int pageIndex) {
    int COLUMNS_PER_ROW = 3;

    GridPane gridPane = new GridPane();
    gridPane.getStyleClass().add("tilePage");

    int position = pageIndex * model.MODULES_PER_PAGE;
    int count = 0;
    int column = 0;
    int row = 0;

    while (count < model.MODULES_PER_PAGE && position < model.getModules().size()) {
      Module module = model.getModules().get(position);
      Node tile = model.getTile(module);
      gridPane.add(tile, column, row);

      position++;
      count++;
      column++;

      if (column == COLUMNS_PER_ROW) {
        column = 0;
        row++;
      }
    }

    return gridPane;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().add(tileBox);
  }

  public void addTile(Node tile) {
    tileBox.getChildren().add(tile);
  }
}
