package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.view.module.TileControl;
import javafx.scene.control.Button;

public class HomePresenter implements Presenter {
  private final WorkbenchFxModel model;
  private final HomeView view;

  public HomePresenter(WorkbenchFxModel model, HomeView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    // For each module's module-tile: When clicking at it, open the module
    view.getChildren().forEach(tileControl -> {
      tileControl.setOnMouseClicked(event -> {
        model.openModule(view.moduleMap.get(tileControl));
      });
    });
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
