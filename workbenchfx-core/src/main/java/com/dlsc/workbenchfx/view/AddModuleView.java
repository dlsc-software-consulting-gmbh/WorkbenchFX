package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.control.Pagination;

/**
 * Shows the home screen with the {@link WorkbenchModule}s as tiles, using pagination.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class AddModuleView extends Pagination implements View {

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
  public final void initializeSelf() {
    setId("add-module-view");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void layoutParts() {
    getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
  }


}
