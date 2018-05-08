package com.dlsc.workbenchfx.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: javadoc
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFx extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchFx.class.getName());

  private WorkbenchFx() {
    getStyleClass().add("workbench-fx");
  }

  public static WorkbenchFx of() {
    return new WorkbenchFx();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new WorkbenchSkin(this);
  }

}
