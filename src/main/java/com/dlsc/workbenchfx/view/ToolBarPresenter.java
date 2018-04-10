package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;

public class ToolBarPresenter implements Presenter{
  private final WorkbenchFxModel model;
  private final ToolBarView view;

  public ToolBarPresenter(WorkbenchFxModel model, ToolBarView view) {
    this.model = model;
    this.view = view;
  }
}
