package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;

public class HomePresenter implements Presenter {
  private final WorkbenchFx model;
  private final HomeView view;

  /**
   * Creates a new {@link HomePresenter} object for a corresponding {@link HomeView}.
   */
  public HomePresenter(WorkbenchFx model, HomeView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.pagination.setPageCount(model.getModules().size() / model.modulesPerPage + 1);
    view.pagination.setPageFactory(model::getPage);
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
