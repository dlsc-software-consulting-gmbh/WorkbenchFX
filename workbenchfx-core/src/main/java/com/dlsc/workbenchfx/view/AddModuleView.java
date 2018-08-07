package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.css.PseudoClass;
import javafx.scene.control.Pagination;

/**
 * Shows the home screen with the {@link WorkbenchModule}s as tiles, using pagination.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class AddModuleView extends Pagination implements View {

  private static final PseudoClass ONE_PAGE_STATE = new PseudoClass() {
    @Override
    public String getPseudoClassName() {
      return "one-page";
    }
  };

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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
  }

  void updatePageCount(int amountOfPages) {
    setPageCount(amountOfPages);
    pseudoClassStateChanged(ONE_PAGE_STATE, amountOfPages == 1);
  }
}
