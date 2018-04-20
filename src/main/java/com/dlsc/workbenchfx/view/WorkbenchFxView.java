package com.dlsc.workbenchfx.view;

import javafx.geometry.Insets;
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
  final ToolBarView toolBarView;
  final HomeView homeView;
  final CenterView centerView;

  /**
   * Displays all of the view parts, representing the master view.
   */
  public WorkbenchFxView(ToolBarView toolBarView, HomeView homeView, CenterView centerView) {
    this.toolBarView = toolBarView;
    this.homeView = homeView;
    this.centerView = centerView;
    init();
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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    setTop(toolBarView);
    setCenter(centerView);

    setPadding(new Insets(10));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
