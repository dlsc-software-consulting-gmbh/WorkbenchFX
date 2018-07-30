package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.util.WorkbenchUtils;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Represents the presenter of the corresponding {@link ContentView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ContentPresenter extends Presenter {
  private final Workbench model;
  private final ContentView view;

  /**
   * Creates a new {@link ContentPresenter} object for a corresponding {@link ContentView}.
   *
   * @param model the workbench, holding all data
   * @param view the corresponding {@link ContentView}
   */
  public ContentPresenter(Workbench model, ContentView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.setAddModuleView();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    model.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
      view.removeToolbar(); // Remove toolbar

      // When the active module changes, the new view is set to the add module screen if null.
      if (Objects.isNull(newModule)) {
        view.setAddModuleView();
      } else {
        // Clear the toolbar's items from last module's items
        view.moduleToolbarControl.clear();

        // Setting content first in terms of z-index
        Node activeModuleView = model.getActiveModuleView();
        VBox.setVgrow(activeModuleView, Priority.ALWAYS);
        view.setContent(activeModuleView);

        // Add all items initially to the toolbar
        newModule.getToolbarControlsLeft().stream().forEachOrdered(
            view.moduleToolbarControl::addToolbarControlLeft
        );
        newModule.getToolbarControlsRight().stream().forEachOrdered(
            view.moduleToolbarControl::addToolbarControlRight
        );

        if (!view.moduleToolbarControl.isEmpty()) { // If it's not empty, add it to the view
          view.addToolbar();
        }

        // Add a listener to the module's toolbar items on the left
        WorkbenchUtils.addSetListener(
            newModule.getToolbarControlsLeft(),
            change -> { // On added, add it to the toolbar and put it into view
              view.moduleToolbarControl.addToolbarControlLeft(change.getElementAdded());
              view.addToolbar();
            },
            change -> { // On removed, remove it from the toolbar
              view.moduleToolbarControl.removeToolbarControlLeft(change.getElementRemoved());
              if (view.moduleToolbarControl.isEmpty()) { // If it's empty, remove it from the view
                view.removeToolbar();
              }
            }
        );

        // Add a listener to the module's toolbar items on the right
        WorkbenchUtils.addSetListener(
            newModule.getToolbarControlsRight(),
            change -> { // On added, add it to the toolbar and put it into view
              view.moduleToolbarControl.addToolbarControlRight(change.getElementAdded());
              view.addToolbar();
            },
            change -> { // On removed, remove it from the toolbar
              view.moduleToolbarControl.removeToolbarControlRight(change.getElementRemoved());
              if (view.moduleToolbarControl.isEmpty()) { // If it's empty, remove it from the view
                view.removeToolbar();
              }
            }
        );
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }
}
