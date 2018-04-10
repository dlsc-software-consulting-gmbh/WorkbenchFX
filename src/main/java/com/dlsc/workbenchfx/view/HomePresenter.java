package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;

public class HomePresenter implements Presenter{
  private final WorkbenchFxModel model;
  private final ToolBarView view;

  public HomePresenter(WorkbenchFxModel model, ToolBarView view) {
    this.model = model;
    this.view = view;
  }
}
