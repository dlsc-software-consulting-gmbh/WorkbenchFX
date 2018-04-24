package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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

    Pagination pagination = new Pagination(model.getModules().size() / model.MODULES_PER_PAGE + 1);
    pagination
    pagination.setPageFactory(pageIndex -> createPage(pageIndex));
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
    GridPane gridPane = new GridPane();
    gridPane.getStyleClass().add("modules-page");

    int position = pageIndex * MODULE_BUTTONS_PER_PAGE;
    int count = 0;
    int column = 0;
    int row = 0;

    while (count < MODULE_BUTTONS_PER_PAGE && position < getSkinnable().getShell().getModules().size()) {
      ShellModule module = getSkinnable().getShell().getModules().get(position);
      final Button button = new Button(module.getName());
      button.getStyleClass().add("module-button");
      button.getStyleClass().add(module.getIconClass());
      button.setContentDisplay(ContentDisplay.TOP);
      gridPane.add(button, column, row);

      if (module.isImplemented()) {
        button.getStyleClass().add("implemented");
      }

      button.setOnAction(evt -> getSkinnable().getShell().openModule(module));

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
