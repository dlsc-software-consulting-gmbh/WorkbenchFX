package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
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
    model.getModules()
        .forEach(module -> module.getTile()
            .setOnMouseClicked(event -> model.openModule(module))
        );
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
