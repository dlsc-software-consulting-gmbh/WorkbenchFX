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
          openModules.add(newModule);
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
    if (!modules.contains(module)) {
      throw new IllegalArgumentException(
          "Module was not passed in with the constructor of WorkbenchFxModel");
    }
    activeModule.setValue(module);
  }

  /**
   * Goes back to the home screen where the user can choose between modules.
   */
  public void openHomeScreen() {
    activeModule.setValue(null);
  }

  /**
   * Closes the {@code module}.
   * @param module to be closed
   * @return true if closing was successful
   */
  public boolean closeModule(Module module) {
    Objects.requireNonNull(module);
    int i = openModules.indexOf(module);
    if (i == -1) {
      throw new IllegalArgumentException("Module has not been loaded yet.");
    }
    // set new active module
    Module active;
    if (openModules.size() == 1) {
      // go to home screen
      active = null;
    } else if (i == 0) {
      // multiple modules open, leftmost is active
      active = openModules.get(i + 1);
    } else {
      active = openModules.get(i - 1);
    }
    // attempt to destroy module
    if (!module.destroy()) {
      // module should or could not be destroyed
      return false;
    } else {
      activeModule.setValue(active);
      return openModules.remove(module);
    }
  }

  public ObservableList<Module> getOpenModules() {
    return FXCollections.unmodifiableObservableList(openModules);
  }

  public ObservableList<Module> getModules() {
    return FXCollections.unmodifiableObservableList(modules);
  }

  public Module getActiveModule() {
    return activeModule.get();
  }

  public ReadOnlyObjectProperty<Module> activeModuleProperty() {
    return activeModule;
  }

  public Node getActiveModuleView() {
    return activeModuleView.get();
  }

  public ReadOnlyObjectProperty<Node> activeModuleViewProperty() {
    return activeModuleView;
  }
}
