package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains presenter logic of the {@link WorkbenchFxView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxPresenter.class.getName());

  private WorkbenchFx model;
  private WorkbenchFxView view;

  /**
   * Constructs a new {@link WorkbenchFxPresenter} for the {@link WorkbenchFxView}.
   *
   * @param model the model of WorkbenchFX
   * @param view  corresponding view to this presenter
   */
  public WorkbenchFxPresenter(WorkbenchFx model, WorkbenchFxView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.centerView.setContent(view.homeView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

    // When the active module changes, the new view is set od the home screen if null.
    model.activeModuleViewProperty().addListener((observable, oldModule, newModule) ->
        view.centerView.setContent(Objects.isNull(newModule) ? view.homeView : newModule)
    );

  }

  /** {@inheritDoc} */
  @Override
  public void setupValueChangedListeners() {
    // Show and hide global menu depending on property
    /*model.globalMenuShownProperty().addListener((observable, oldShown, newShown) -> {
              if (newShown) {
                addOverlay(model.getGlobalMenu());
              } else {
                removeOverlay(model.getGlobalMenu());
              }
            });
    view.getChildren()
        .addListener(
            (ListChangeListener<? super Node>)
                c -> {
                  if (view.getChildren().size() == 2) { // TODO: magic number
                    // only viewBox and glassPane are left
                    model.setGlassPaneShown(false);
                  } else {
                    // at least one overlay is being shown
                    model.setGlassPaneShown(true);
                  }
                  while (c.next()) {
                    if (c.wasRemoved()) {
                      for (Node node : c.getRemoved()) {
                        if (node == model.getGlobalMenu()) {
                          model.setGlobalMenuShown(false);
                        }
                      }
                    }
                  }
                });*/
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    view.glassPane.hideProperty().bind(model.glassPaneShownProperty().not());
  }
}
