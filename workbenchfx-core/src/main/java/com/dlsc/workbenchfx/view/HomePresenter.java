package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;

/**
 * Represents the presenter of the corresponding {@link HomeView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class HomePresenter extends Presenter {
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
    view.pagination.setPageCount(model.getAmountOfPages());
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
