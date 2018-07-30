package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
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
        Node activeModuleView = model.getActiveModuleView();
        view.setContent(activeModuleView);
        VBox.setVgrow(activeModuleView, Priority.ALWAYS);

        view.setModuleInToolbar(newModule);
        // Adding the listener
        view.toolbarEmptyProperty().addListener((observable1, wasEmpty, isEmpty) -> {
          if (!isEmpty) {
            view.addToolbar();
          }
        });
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
