package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HomeView extends StackPane implements View {
  private final WorkbenchFx model;
  AnchorPane tilePane;

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
    int pageCount = model.getModules().size() / model.MODULES_PER_PAGE + 1;
    Pagination pagination = new Pagination(pageCount);
    pagination.setPageFactory(model::getPage);
    pagination.setMaxPageIndicatorCount(100);
    pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

    tilePane = new AnchorPane();
    tilePane.setId("tilePane");
    AnchorPane.setTopAnchor(pagination, 0.0);
    AnchorPane.setRightAnchor(pagination, 10.0);
    AnchorPane.setBottomAnchor(pagination, 60.0);
    AnchorPane.setLeftAnchor(pagination, 10.0);
    tilePane.getChildren().addAll(pagination);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().add(tilePane);
  }

  public void addTile(Node tile) {
    tilePane.getChildren().add(tile);
  }
}
