package com.dlsc.workbenchfx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the model which holds all of the data and logic which is not limited to presenters.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxModel {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFxModel.class.getName());

  private final ObservableList<Module> modules = FXCollections.observableArrayList();

  /**
   * Initializes a new model.
   */
  public WorkbenchFxModel(Module... modules) {
    this.modules.addAll(modules);
  }

  public int add(int a, int b) {
    return a + b;
  }

}
