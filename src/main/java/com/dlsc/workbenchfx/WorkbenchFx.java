package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import com.dlsc.workbenchfx.view.CenterPresenter;
import com.dlsc.workbenchfx.view.CenterView;
import com.dlsc.workbenchfx.view.HomePresenter;
import com.dlsc.workbenchfx.view.HomeView;
import com.dlsc.workbenchfx.view.ToolBarPresenter;
import com.dlsc.workbenchfx.view.ToolBarView;
import com.dlsc.workbenchfx.view.WorkbenchFxPresenter;
import com.dlsc.workbenchfx.view.WorkbenchFxView;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the main WorkbenchFX class.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFx {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFx.class.getName());
  private final Module[] modules;

  private WorkbenchFxModel workbenchFxModel;

  private ToolBarView toolBarView;
  private ToolBarPresenter toolBarPresenter;

  private HomeView homeView;
  private HomePresenter homePresenter;

  private CenterView centerView;
  private CenterPresenter centerPresenter;

  private WorkbenchFxView workbenchFxView;
  private WorkbenchFxPresenter workbenchFxPresenter;

  private WorkbenchFx(Module... modules) {
    this.modules = modules;

    toolBarView = new ToolBarView(workbenchFxModel, modules, this);
    toolBarPresenter = new ToolBarPresenter(workbenchFxModel, toolBarView);

    homeView = new HomeView(workbenchFxModel, modules, this);
    homePresenter = new HomePresenter(workbenchFxModel, homeView);

    centerView = new CenterView(workbenchFxModel, modules, this);
    centerPresenter = new CenterPresenter(workbenchFxModel, centerView);

    workbenchFxView = new WorkbenchFxView(toolBarView, homeView, centerView);
    workbenchFxPresenter = new WorkbenchFxPresenter(workbenchFxModel, modules, workbenchFxView, this);
  }

  /**
   * Creates the Workbench window.
   */
  public static WorkbenchFx of(Module... modules) {
    return new WorkbenchFx(modules);
  }

  public Node getView() {
    return workbenchFxView;
  }
}
