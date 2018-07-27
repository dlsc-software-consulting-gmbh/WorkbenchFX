package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Page;

/**
 * Represents the presenter of the corresponding {@link AddModuleView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class AddModulePresenter extends Presenter {
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
  public void initializeViewParts() {
    view.pagination.setPageCount(model.getAmountOfPages());
    view.pagination.setPageFactory(pageIndex -> {
      Page page = model.getPageFactory().call(model);
      page.setPageIndex(pageIndex);
      return page;
    });
    view.pagination.setMaxPageIndicatorCount(Integer.MAX_VALUE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    view.pagination.pageCountProperty().bind(model.amountOfPagesProperty());
  }

}
