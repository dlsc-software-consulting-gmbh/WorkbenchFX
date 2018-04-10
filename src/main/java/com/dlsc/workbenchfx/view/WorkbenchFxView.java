package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the master view, which is used to show all view parts. TODO
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxView extends BorderPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxView.class.getName());

  private WorkbenchFxModel model;
  private Module testModule;
  private WorkbenchFx workbench;
  private TabControl tabcon;
  private TileControl tilecon;
  private Node testView;

  /**
   * Displays all of the view parts, representing the master view.
   *
   * @param model the model of WorkbenchFX
   */
  public WorkbenchFxView(WorkbenchFxModel model, Module testModule, WorkbenchFx workbench) {
    this.model = model;
    this.testModule = testModule;
    this.workbench = workbench;
    init();
  }

  public WorkbenchFxView(ToolBarView toolBarView, CenterView centerView) {
    setTop(toolBarView);
    setCenter(centerView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    tabcon = (TabControl) testModule.getTab();
    tilecon = (TileControl) testModule.getTile();
    testView = testModule.init(workbench);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    setTop(tabcon);
    setLeft(tilecon);
    setCenter(testView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
