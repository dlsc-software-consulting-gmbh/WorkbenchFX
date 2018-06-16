package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Page;

/**
 * Represents the presenter of the corresponding {@link HomeView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class HomePresenter implements Presenter {
  private final Workbench model;
  private final HomeView view;

  /**
   * Creates a new {@link HomePresenter} object for a corresponding {@link HomeView}.
   */
  public HomePresenter(Workbench model, HomeView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.pagination.setPageCount(model.amountOfPages());
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

  }

}
