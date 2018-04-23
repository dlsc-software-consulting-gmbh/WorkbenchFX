package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;

public class HomePresenter implements Presenter {
  private final WorkbenchFx model;
  private final HomeView view;

  /**
   * Creates a new {@link HomePresenter} object for a corresponding {@link HomeView}.
   */
  public HomePresenter(WorkbenchFx model, HomeView view) {
    this.model = model;
    this.view = view;
    init();
  }

  @Override
  public void initializeViewParts() {
    // Adds the module-tiles to the view
    model.getModules().forEach(module -> {
      Node tileControl = model.getTile(module);
      tileControl.getStyleClass().add("tileControl");
      view.getChildren().add(tileControl);
    });
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
