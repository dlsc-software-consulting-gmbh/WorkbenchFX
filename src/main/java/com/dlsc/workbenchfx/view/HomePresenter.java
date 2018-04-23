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

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    // Adds the module-tiles to the view
    model.getModules().forEach(module -> view.getChildren().add(model.getTile(module)));
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
