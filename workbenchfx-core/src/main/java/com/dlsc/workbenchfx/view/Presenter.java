package com.dlsc.workbenchfx.view;

/**
 * Defines a presenter of WorkbenchFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public abstract class Presenter {

  /**
   * Calls all the other methods for easier initialization.
   */
  void init() {
    initializeViewParts();
    setupEventHandlers();
    setupValueChangedListeners();
    setupBindings();
  }

  /**
   * Initializes parts of the view which require more logic.
   */
  void initializeViewParts() {
  }

  /**
   * Sets up event handlers of the view.
   */
  void setupEventHandlers() {
  }

  /**
   * Adds all listeners to view elements and model properties.
   */
  void setupValueChangedListeners() {
  }

  /**
   * Sets up bindings of the view.
   */
  void setupBindings() {
  }

}
