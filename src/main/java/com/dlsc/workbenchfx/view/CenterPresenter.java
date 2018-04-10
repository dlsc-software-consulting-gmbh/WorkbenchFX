package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;

public class CenterPresenter implements Presenter{
  private final WorkbenchFxModel model;
  private final ToolBarView view;

  public CenterPresenter(WorkbenchFxModel model, ToolBarView view) {
    this.model = model;
    this.view = view;
  }
}
