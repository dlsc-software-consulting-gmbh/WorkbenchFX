package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.module.Module;
import javafx.collections.ListChangeListener;

public class ToolBarPresenter implements Presenter {
  private final WorkbenchFx model;
  private final ToolBarView view;

  public ToolBarPresenter(WorkbenchFx model, ToolBarView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    model.getOpenModules().forEach(module -> view.getChildren().add(module.getTab()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    // When the home button is clicked, the view changes
    view.homeBtn.setOnAction(event -> model.openModule(null));

    model.getOpenModules().addListener((ListChangeListener<? super Module>) change -> {
//      https://docs.oracle.com/javase/8/javafx/api/javafx/collections/ListChangeListener.Change.html
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
