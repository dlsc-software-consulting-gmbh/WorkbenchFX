package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;

/**
 * Represents the presenter of the corresponding {@link CenterView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CenterPresenter implements Presenter {
  private final WorkbenchFx model;
  private final CenterView view;

  /**
   * Creates a new {@link CenterPresenter} object for a corresponding {@link CenterView}.
   */
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
