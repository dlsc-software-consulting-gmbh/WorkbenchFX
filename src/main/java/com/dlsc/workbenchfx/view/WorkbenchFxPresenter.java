package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
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

  private WorkbenchFxModel model;
  private WorkbenchFxView workbenchFxView;

  /**
   * Constructs a new presenter for the {@link WorkbenchFxView}.
   *
   * @param model           the model of WorkbenchFX
   * @param workbenchFxView corresponding view to this presenter
   */
  public WorkbenchFxPresenter(WorkbenchFxModel model, WorkbenchFxView workbenchFxView) {
    this.model = model;
    this.workbenchFxView = workbenchFxView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {

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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }
}
