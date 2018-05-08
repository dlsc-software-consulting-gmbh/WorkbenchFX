package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;

/**
 * Represents the presenter of the corresponding {@link ContentView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ContentPresenter implements Presenter {
  private final Workbench model;
  private final ContentView view;

  /**
   * Creates a new {@link ContentPresenter} object for a corresponding {@link ContentView}.
   */
  public ContentPresenter(Workbench model, ContentView view) {
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
