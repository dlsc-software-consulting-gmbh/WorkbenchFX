package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.view.WorkbenchFxPresenter;
import com.dlsc.workbenchfx.view.WorkbenchFxView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the main WorkbenchFX class.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFx extends BorderPane {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFx.class.getName());

  private WorkbenchFxModel workbenchFxModel;

  private WorkbenchFxView workbenchFxView;
  private WorkbenchFxPresenter workbenchFxPresenter;

  private WorkbenchFx() {
    workbenchFxView = new WorkbenchFxView(workbenchFxModel);
    workbenchFxPresenter = new WorkbenchFxPresenter(workbenchFxModel, workbenchFxView);
  }

  /**
   * Creates the Workbench window.
   */
  public static WorkbenchFx of() {
    return new WorkbenchFx();
  }

  public Node getView() {
    TestModule tm = new TestModule();
    return new MyView(tm, this);
  }
}
