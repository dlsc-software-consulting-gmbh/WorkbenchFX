package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.model.module.Module;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
   * When the home screen is being displayed, {@code activeModule} and {@code activeModuleView}
   * are null.
   */
  private final ObjectProperty<Module> activeModule = new SimpleObjectProperty<>();
  private final ObjectProperty<Node> activeModuleView = new SimpleObjectProperty<>();

  /**
   * Initializes a new model.
   */
  public WorkbenchFxModel(Module... modules) {
    this.modules.addAll(modules);
    initLifecycle();
  }

  private void initLifecycle() {
    activeModule.addListener((observable, oldModule, newModule) -> {
      if (oldModule != newModule) {
        if (oldModule != null) {
          // a different module is currently active
          oldModule.deactivate();
        }
        if (newModule == null) {
          // switch to home screen
          activeModuleView.setValue(null);
          return;
        }
        if (!openModules.contains(newModule)) {
          // module has not been loaded yet
          newModule.init(this);
        }
        activeModuleView.setValue(newModule.activate());
      }
    });
  }

  /**
   * Opens the {@code module} in a new tab, if it isn't initialized yet or else opens the tab of it.
   * @param module the module to be opened or null to go to the home view
   */
  public void openModule(Module module) {
    activeModule.setValue(module);
  }

  /**
   * Closes the {@code module}.
   * @param module to be closed
   */
  public void closeModule(Module module) {
    Objects.requireNonNull(module);
    int i = openModules.indexOf(module);
    if (i == -1) {
      throw new IllegalArgumentException("Module has not been loaded yet.");
    }
    // set new active module
    if (openModules.size() == 1) {
      // go to home screen
      activeModule.setValue(null);
    } else if (i == 0) {
      // multiple modules open, leftmost is active
      activeModule.setValue(openModules.get(i + 1));
    } else {
      activeModule.setValue(openModules.get(i - 1));
    }
  }

  public ObservableList<Module> getModules() {
    return FXCollections.unmodifiableObservableList(modules);
  }

  public ReadOnlyObjectProperty<Node> activeModuleViewProperty() {
    return activeModuleView;
  }
}
