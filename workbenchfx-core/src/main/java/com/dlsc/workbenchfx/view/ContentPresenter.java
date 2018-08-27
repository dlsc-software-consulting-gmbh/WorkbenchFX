package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.util.WorkbenchUtils;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the presenter of the corresponding {@link ContentView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class ContentPresenter extends Presenter {

  private static final Logger LOGGER =
      LogManager.getLogger(ContentPresenter.class.getName());

  private final Workbench model;
  private final ContentView view;
  private final ObservableList<WorkbenchModule> openModules;

  /**
   * Creates a new {@link ContentPresenter} object for a corresponding {@link ContentView}.
   *
   * @param model the workbench, holding all data
   * @param view the corresponding {@link ContentView}
   */
  public ContentPresenter(Workbench model, ContentView view) {
    this.model = model;
    this.view = view;
    openModules = model.getOpenModules();
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeViewParts() {
    view.setAddModuleView();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupValueChangedListeners() {
    model.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
      view.showToolbar(false); // Remove toolbar
      view.hideActiveView();

      if (Objects.isNull(newModule)) {
        // The active module is null -> therefore setting the addModuleView
        view.setAddModuleView();
      } else {
        // The active Module is not null -> therefore setting the view of the module
        Node activeModuleView = model.getActiveModuleView();
        view.setContent(activeModuleView);
        VBox.setVgrow(activeModuleView, Priority.ALWAYS);

        // Setting the new chosen module in the toolbar -> the content of the toolbar changes
        // Unbind Modules, which were set before
        view.toolbarControl.toolbarControlsLeftProperty().unbind();
        view.toolbarControl.toolbarControlsRightProperty().unbind();

        // Bind new Module
        view.toolbarControl.toolbarControlsLeftProperty()
            .bindContent(newModule.getToolbarControlsLeft());
        view.toolbarControl.toolbarControlsRightProperty()
            .bindContent(newModule.getToolbarControlsRight());

        // Initially add the toolbar, if its not empty
        view.showToolbar(!view.toolbarControl.isEmpty());

        // Adding the listener -> add/remove the toolbar when empty
        view.toolbarControl.emptyProperty().addListener(
            (observable1, wasEmpty, isEmpty) -> view.showToolbar(!isEmpty)
        );
      }
    });

    WorkbenchUtils.addListListener(openModules, module -> {}, module -> {
      LOGGER.trace("Remove from scene graph view of module: " + model.getActiveModule());
      view.removeView(model.getActiveModuleView());
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setupBindings() {

  }
}
