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

  private final ObservableList<CapsuleBase> modules = FXCollections.observableArrayList();

  /**
   * Initializes a new model.
   */
  public WorkbenchFxModel(CapsuleBase... modules) {
    this.modules.addAll(modules);
  }

}
