package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.collections.ListChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DropdownPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(DropdownPresenter.class.getName());
  private final WorkbenchFx model;
  private final DropdownView view;

  /**
   * Creates a new {@link DropdownPresenter} object for a corresponding {@link ToolBarView}.
   */
  public DropdownPresenter(WorkbenchFx model, DropdownView view) {
    this.model = model;
    this.view = view;
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
    // When the List of the currently open toolBarControls is changed, the view is updated.
    model.getDropdowns().addListener((ListChangeListener<? super Dropdown>) c -> {
      while (c.next()) {
        if (c.wasRemoved()) {
          for (Dropdown dropdown : c.getRemoved()) {
            LOGGER.debug("Dropdown " + dropdown + " removed");
            view.removeDropdown(c.getFrom());
          }
        }
        if (c.wasAdded()) {
          for (Dropdown dropdown : c.getAddedSubList()) {
            LOGGER.debug("Dropdown " + dropdown + " added");
            view.addDropdown(dropdown);
          }
        }
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
