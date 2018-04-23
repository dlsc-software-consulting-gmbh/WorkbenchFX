package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;

public class CenterPresenter implements Presenter {
  private final WorkbenchFx model;
  private final CenterView view;

  public CenterPresenter(WorkbenchFx model, CenterView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {

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
