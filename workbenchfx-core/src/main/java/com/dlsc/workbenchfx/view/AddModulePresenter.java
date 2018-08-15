package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Page;
import javafx.css.PseudoClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the presenter of the corresponding {@link AddModuleView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class AddModulePresenter extends Presenter {

  private static final Logger LOGGER =
      LogManager.getLogger(AddModulePresenter.class.getName());

  private static final PseudoClass ONE_PAGE_STATE = PseudoClass.getPseudoClass("one-page");

  private final Workbench model;
  private final AddModuleView view;

  /**
   * Creates a new {@link AddModulePresenter} object for a corresponding {@link AddModuleView}.
   *
   * @param model the workbench, holding all data
   * @param view the corresponding {@link AddModuleView}
   */
  public AddModulePresenter(Workbench model, AddModuleView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeViewParts() {
    updatePageCount(model.getAmountOfPages());

    view.setPageFactory(pageIndex -> {
      Page page = model.getPageFactory().call(model);
      page.setPageIndex(pageIndex);
      return page;
    });
    view.setMaxPageIndicatorCount(Integer.MAX_VALUE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupValueChangedListeners() {
    model.amountOfPagesProperty().addListener(
        (observable, oldPageCount, newPageCount) -> updatePageCount(newPageCount.intValue()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupBindings() {

  }

  private void updatePageCount(int amountOfPages) {
    view.setPageCount(amountOfPages);
    view.pseudoClassStateChanged(ONE_PAGE_STATE, amountOfPages == 1);
  }
}
