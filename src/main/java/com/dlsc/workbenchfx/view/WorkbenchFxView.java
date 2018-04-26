package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the master view, which is used to show all view parts. TODO
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxView extends StackPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxView.class.getName());
  private final WorkbenchFx model;
  final ToolBarView toolBarView;
  final HomeView homeView;
  final CenterView centerView;
  final GlassPane glassPane;
  VBox viewBox;

  /**
   * Displays all of the view parts, representing the master view.
   */
  public WorkbenchFxView(WorkbenchFx model, ToolBarView toolBarView, HomeView homeView, CenterView centerView, GlassPane glassPane) {
    this.model = model;
    this.toolBarView = toolBarView;
    this.homeView = homeView;
    this.centerView = centerView;
    this.glassPane = glassPane;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("workbench");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    viewBox = new VBox();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    viewBox.getChildren().addAll(toolBarView, centerView);
    getChildren().addAll(viewBox, glassPane);
    VBox.setVgrow(centerView, Priority.ALWAYS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
