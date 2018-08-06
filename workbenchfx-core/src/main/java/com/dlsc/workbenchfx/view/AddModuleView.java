package com.dlsc.workbenchfx.view;

import javafx.scene.control.Pagination;
import javafx.scene.layout.StackPane;

/**
 * Shows the home screen with the {@link Module}s as tiles, using pagination.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class AddModuleView extends StackPane implements View {
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
    getChildren().add(pagination);
  }
}
