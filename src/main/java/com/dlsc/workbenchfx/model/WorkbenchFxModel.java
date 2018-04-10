package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.model.module.Module;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

  /**
   * List of all modules.
   */
  private final ObservableList<Module> modules = FXCollections.observableArrayList();

  /**
   * List of all currently open modules.
   * Open modules are being displayed as open tabs in the application.
   */
  private final ObservableList<Module> openModules = FXCollections.observableArrayList();

  /**
   * Currently active module.
   * Active module is the module, which is currently being displayed in the view.
   */
  private final ObjectProperty<Module> activeModule = new SimpleObjectProperty<>();

  /**
   * Initializes a new model.
   */
  public WorkbenchFxModel(Module... modules) {
    this.modules.addAll(modules);
  }

  public void openModule(Module module) {
    Objects.requireNonNull(module);
    Module currentModule = activeModule.get();
    if (currentModule == module) {
      return; // an already open module can't be opened again
    }
    // Follow lifecycle
    if (currentModule != null) {
      // a different module is currently active
      currentModule.deactivate();
    }
    if (!openModules.contains(module)) {
      // module has not been loaded yet
      module.init(this);
    }
    activeModule.setValue(module);
    module.activate();
  }

}
