package com.dlsc.workbenchfx.modules.patient.view.util;

import java.util.List;

/**
 * @author Dieter Holz
 */
public interface ViewMixin {

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

  default void addStylesheetFiles(String... stylesheetFile) {
    for (String file : stylesheetFile) {
      String stylesheet = getClass().getResource(file).toExternalForm();
      getStylesheets().add(stylesheet);
    }
  }

  List<String> getStylesheets();
}
