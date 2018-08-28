package com.dlsc.workbenchfx.modules.patient.view.util.numberrange;

import javafx.scene.Parent;

/**
 * @author Dieter Holz
 */
public interface CustomControlMixin {
  default void init() {
    initializeSelf();
    initializeParts();
    layoutParts();
    setupEventHandlers();
    setupValueChangedListeners();
    setupBindings();
  }

  default void initializeSelf() {
  }

  void initializeParts();

  void layoutParts();

  default void setupEventHandlers() {
  }

  default void setupValueChangedListeners() {
  }

  default void setupBindings() {
  }

  default void addStylesheetFiles(Parent parent, String... stylesheetFile) {
    for (String file : stylesheetFile) {
      String stylesheet = getClass().getResource(file).toExternalForm();
      parent.getStylesheets().add(stylesheet);
    }
  }

}
