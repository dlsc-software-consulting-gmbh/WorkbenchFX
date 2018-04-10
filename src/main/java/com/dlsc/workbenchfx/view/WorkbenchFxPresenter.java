package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains presenter logic of the {@link WorkbenchFxView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxPresenter.class.getName());

  private WorkbenchFxModel model;
  private Module[] modules;
  private WorkbenchFxView view;
  private WorkbenchFx workbench;

  /**
   * Constructs a new presenter for the {@link WorkbenchFxView}.
   * @param model           the model of WorkbenchFX
   * @param modules
   * @param view corresponding view to this presenter
   * @param workbench
   */
  public WorkbenchFxPresenter(WorkbenchFxModel model, Module[] modules, WorkbenchFxView view, WorkbenchFx workbench) {
    this.model = model;
    this.modules = modules;
    this.view = view;
    this.workbench = workbench;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.centerView.setContentNode(view.homeView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    view.toolBarView.homeBtn.setOnAction(event -> {
      view.centerView.setContentNode(view.homeView);
    });

    Button b = (Button) modules[0].getTile();
    b.setOnAction(event -> {
      view.centerView.setContentNode(modules[0].init(workbench));
      view.toolBarView.trigger(modules[0].getTab());
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
