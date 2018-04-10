package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import com.dlsc.workbenchfx.view.WorkbenchFxPresenter;
import com.dlsc.workbenchfx.view.WorkbenchFxView;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
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

  private WorkbenchFxView workbenchFxView;
  private WorkbenchFxPresenter workbenchFxPresenter;

  private WorkbenchFx(Module... modules) {
    this.modules = modules;

    workbenchFxView = new WorkbenchFxView(workbenchFxModel, modules[0], this);
    workbenchFxPresenter = new WorkbenchFxPresenter(workbenchFxModel, workbenchFxView);
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
