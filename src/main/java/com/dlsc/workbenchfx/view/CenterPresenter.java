package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;

public class CenterPresenter implements Presenter {
  private final WorkbenchFx model;
  private final CenterView view;

  public CenterPresenter(WorkbenchFx model, CenterView view) {
    this.model = model;
    this.view = view;
  }
}
