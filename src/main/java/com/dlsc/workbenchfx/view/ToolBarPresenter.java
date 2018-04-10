package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;

public class ToolBarPresenter implements Presenter {
  private final WorkbenchFxModel model;
  private final ToolBarView view;

  public ToolBarPresenter(WorkbenchFxModel model, ToolBarView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    // When the home button is clicked, the view changes
    view.homeBtn.setOnAction(event -> model.openModule(null));
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
