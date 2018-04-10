package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import java.util.Objects;
import javafx.scene.Node;

public class CenterPresenter implements Presenter {
  private final WorkbenchFxModel model;
  private final CenterView view;

  public CenterPresenter(WorkbenchFxModel model, CenterView view) {
    this.model = model;
    this.view = view;
  }
}
