package com.dlsc.workbenchfx.view;

import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Shows the home screen with the {@link Module}s as tiles, using pagination.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class AddModuleView extends StackPane implements View {
  AnchorPane tilePane;
  Pagination pagination;

  /**
   * Creates a new {@link AddModuleView}.
   */
  public AddModuleView() {
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("add-module-view");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    pagination = new Pagination();

    tilePane = new AnchorPane();
    tilePane.setId("tile-pane");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    AnchorPane.setTopAnchor(pagination, 0.0);
    AnchorPane.setRightAnchor(pagination, 10.0);
    AnchorPane.setBottomAnchor(pagination, 60.0);
    AnchorPane.setLeftAnchor(pagination, 10.0);

    pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

    tilePane.getChildren().addAll(pagination);

    getChildren().add(tilePane);
  }
}
