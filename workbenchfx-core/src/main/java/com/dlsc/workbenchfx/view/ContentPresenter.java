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

      if (Objects.isNull(newModule)) {
        // The active module is null -> therefore setting the addModuleView
        view.setAddModuleView();
      } else {
        // The active Module is !null -> therefore setting the chosen view
        Node activeModuleView = model.getActiveModuleView();
        view.setContent(activeModuleView);
        VBox.setVgrow(activeModuleView, Priority.ALWAYS);

        // Setting the new chosen module in the toolbar -> the content of the toolbar changes
        view.setModuleInToolbar(newModule);
        if (!view.toolbarEmptyProperty().get()) {
          view.addToolbar(); // Initially add the toolbar, if its not empty
        }

        // Adding the listener -> add/remove the toolbar when empty
        view.toolbarEmptyProperty().addListener((observable1, wasEmpty, isEmpty) -> {
          if (!isEmpty) {
            view.addToolbar();
          } else {
            view.removeToolbar();
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
