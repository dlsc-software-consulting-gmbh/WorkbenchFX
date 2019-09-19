package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.AddModulePresenter;
import com.dlsc.workbenchfx.view.AddModuleView;
import com.dlsc.workbenchfx.view.ContentPresenter;
import com.dlsc.workbenchfx.view.ContentView;
import com.dlsc.workbenchfx.view.ToolbarPresenter;
import com.dlsc.workbenchfx.view.ToolbarView;
import com.dlsc.workbenchfx.view.WorkbenchPresenter;
import com.dlsc.workbenchfx.view.WorkbenchView;
import com.dlsc.workbenchfx.view.controls.selectionstrip.SelectionStrip;
import javafx.application.Platform;
import javafx.scene.control.SkinBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Represents the Skin which is made for the {@link Workbench}.
 * It creates all the Views and Presenters which are needed and sets the stylesheets.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */

public final class WorkbenchSkin extends SkinBase<Workbench> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(WorkbenchSkin.class.getName());

  // Views
  private SelectionStrip<WorkbenchModule> tabBar;

  private ToolbarView toolbarView;
  private ToolbarPresenter toolbarPresenter;

  private AddModuleView addModuleView;
  private AddModulePresenter addModulePresenter;

  private ContentView contentView;
  private ContentPresenter contentPresenter;

  private WorkbenchView workbenchView;
  private WorkbenchPresenter workbenchPresenter;

  /**
   * Creates a skin for a given {@link Workbench}.
   * Contains all views and presenters and sets also the default stylesheet.
   *
   * @param workbench for which this skin is created
   */
  public WorkbenchSkin(Workbench workbench) {
    super(workbench);

    initViews(workbench);

    getChildren().add(workbenchView);

    // if there is only one module in the workbench, open it automatically
    if (workbench.getModules().size() == 1){
      Platform.runLater(() -> workbench.openModule(workbench.getModules().get(0)));
    }
  }

  private void initViews(Workbench model) {
    toolbarView = new ToolbarView(model.getModules().size() > 1);
    toolbarPresenter = new ToolbarPresenter(model, toolbarView);

    addModuleView = new AddModuleView();
    addModulePresenter = new AddModulePresenter(model, addModuleView);

    contentView = new ContentView(addModuleView);
    contentPresenter = new ContentPresenter(model, contentView);

    workbenchView = new WorkbenchView(toolbarView, addModuleView, contentView);
    workbenchPresenter = new WorkbenchPresenter(model, workbenchView);
  }

}
