package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.CenterPresenter;
import com.dlsc.workbenchfx.view.CenterView;
import com.dlsc.workbenchfx.view.HomePresenter;
import com.dlsc.workbenchfx.view.HomeView;
import com.dlsc.workbenchfx.view.ToolBarPresenter;
import com.dlsc.workbenchfx.view.ToolBarView;
import com.dlsc.workbenchfx.view.WorkbenchFxPresenter;
import com.dlsc.workbenchfx.view.WorkbenchFxView;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the main WorkbenchFX class.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFx extends StackPane {
  private static final Logger LOGGER = LogManager.getLogger(WorkbenchFx.class.getName());

  // Views
  private ToolBarView toolBarView;
  private ToolBarPresenter toolBarPresenter;

  private HomeView homeView;
  private HomePresenter homePresenter;

  private CenterView centerView;
  private CenterPresenter centerPresenter;

  private WorkbenchFxView workbenchFxView;
  private WorkbenchFxPresenter workbenchFxPresenter;

  // Modules
  /**
   * List of all modules.
   */
  private final ObservableList<Module> modules = FXCollections.observableArrayList();

  /**
   * List of all currently open modules. Open modules are being displayed as open tabs in the
   * application.
   */
  private final ObservableList<Module> openModules = FXCollections.observableArrayList();

  /**
   * Currently active module. Active module is the module, which is currently being displayed in the
   * view. When the home screen is being displayed, {@code activeModule} and {@code
   * activeModuleView} are null.
   */
  private final ObjectProperty<Module> activeModule = new SimpleObjectProperty<>();
  private final ObjectProperty<Node> activeModuleView = new SimpleObjectProperty<>();

  /**
   * The factories which are called when creating Tabs and Tiles for the Views.
   * They require a module whose attributes are used to create the Nodes.
   */
  private ObjectProperty<Callback<Module, Node>> tabFactory =
      new SimpleObjectProperty<>(this, "tabFactory");
  private ObjectProperty<Callback<Module, Node>> tileFactory =
      new SimpleObjectProperty<>(this, "tileFactory");

  /**
   * Creates the Workbench window.
   */
  public static WorkbenchFx of(Module... modules) {
    return new WorkbenchFx(modules);
  }

  private WorkbenchFx(Module... modules) {
    initModules(modules);

    setTabFactory(module -> {
      TabControl tabControl = new TabControl(module);
      setupRequests(tabControl, module);
      return tabControl;
    });

    setTileFactory(module -> {
      TileControl tileControl = new TileControl(module);
      setupRequests(tileControl, module);
      return tileControl;
    });

    initViews();
    getChildren().add(workbenchFxView);
  }

  private void initModules(Module... modules) {
    this.modules.addAll(modules);

    // handle changes of the active module
    activeModule.addListener(
        (observable, oldModule, newModule) -> {
          if (oldModule != newModule) {
            boolean isDestroyed = !openModules.contains(oldModule);
            if (oldModule != null && !isDestroyed) {
              // switch from one module to another
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

  private TabControl setupRequests(TabControl tabControl, Module module) {
    tabControl.setOnCloseRequest(e -> closeModule(module));
    tabControl.setOnActiveRequest(e -> openModule(module));
    return tabControl;
  }

  private TileControl setupRequests(TileControl tileControl, Module module) {
    tileControl.setOnActiveRequest(e -> openModule(module));
    return tileControl;
  }


  private void initViews() {
    toolBarView = new ToolBarView(this);
    toolBarPresenter = new ToolBarPresenter(this, toolBarView);

    homeView = new HomeView(this);
    homePresenter = new HomePresenter(this, homeView);

    centerView = new CenterView(this);
    centerPresenter = new CenterPresenter(this, centerView);

    workbenchFxView = new WorkbenchFxView(toolBarView, homeView, centerView);
    workbenchFxPresenter = new WorkbenchFxPresenter(this, workbenchFxView);
  }

  /**
   * Opens the {@code module} in a new tab, if it isn't initialized yet or else opens the tab of it.
   *
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
   *
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

  /**
   * The method is called from the views when they need a TabControl to display.
   * Each module generates its own Tab.
   *
   * @param module the module for which the Tab should be created
   * @return a corresponding Tab which contains the values of the module
   */
  public Node getTab(Module module) {
    return tabFactory.get().call(module);
  }

  /**
   * The method is called from the views when they need a TileControl to display.
   * Each module generates its own Tile.
   *
   * @param module the module for which the Tile should be created
   * @return a corresponding Tile which contains the values of the module
   */
  public Node getTile(Module module) {
    return tileFactory.get().call(module);
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

  /**
   * Sets the value of the {@code tabFactory} using the given callback.
   * The callback defines the way the Tabs are created.
   *
   * @param value the callback to be set
   */
  public final void setTabFactory(Callback<Module, Node> value) {
    tabFactory.set(value);
  }

  /**
   * Sets the value of the {@code tileFactory} using the given callback.
   *
   * @param value the callback which defines the way the Tiles are created
   */
  public final void setTileFactory(Callback<Module, Node> value) {
    tileFactory.set(value);
  }
}
