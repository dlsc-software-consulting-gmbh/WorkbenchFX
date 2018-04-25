package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.scene.Node;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    // Show and hide global menu depending on property
    model.globalMenuShownProperty().addListener((observable, oldShown, newShown) -> {
      if (newShown) {
        addOverlay(model.getGlobalMenu());
      } else {
        removeOverlay(model.getGlobalMenu());
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    view.glassPane.hideProperty().bind(model.glassPaneShownProperty().not());
  }

  /**
   * TODO
   */
  public void addOverlay(Node view) {
    this.view.getChildren().add(view);
    model.setGlassPaneShown(true);
  }

  /**
   * TODO
   */
  public void removeOverlay(Node view) {
    this.view.getChildren().remove(view);
    model.setGlassPaneShown(false);
  }

  /**
   * TODO
   */
  public void removeAllOverlays() {
    view.getChildren().removeAll(
        view.getChildren().stream()
        .skip(2) // the viewBox and glasspane
        .collect(Collectors.toList())
    );
    model.setGlassPaneShown(false);
    model.setGlobalMenuShown(false);
  }
}
