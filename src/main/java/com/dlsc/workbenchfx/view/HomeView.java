package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class HomeView extends HBox implements View {
  private final WorkbenchFx model;

  /**
   * The Constructor of the {@code HomeView}.
   */
  public HomeView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  @Override
  public void initializeParts() {
    getStylesheets().add("./com/dlsc/workbenchfx/css/main.css");
  }

  @Override
  public void layoutParts() {
    setSpacing(50);
    setPadding(new Insets(50));
  }
}
