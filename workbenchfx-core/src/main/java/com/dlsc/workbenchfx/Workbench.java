package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import com.dlsc.workbenchfx.view.controls.module.Page;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains all the model logic for the workbench.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Workbench extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(Workbench.class.getName());

  public static final String STYLE_CLASS_ACTIVE_TAB = "active-tab";
  public static final String STYLE_CLASS_ACTIVE_HOME = "active-home";

  // Custom Controls
  private Node navigationDrawer;

  // Lists
  private final ObservableSet<Node> toolbarControlsRight =
      FXCollections.observableSet(new LinkedHashSet<>());
  private final ObservableSet<Node> toolbarControlsLeft =
      FXCollections.observableSet(new LinkedHashSet<>());
  private final ObservableList<MenuItem> navigationDrawerItems =
      FXCollections.observableArrayList();

  /**
   * Map containing all overlays which have been loaded into the scene graph, with their
   * corresponding {@link GlassPane}.
   */
  private final ObservableMap<Node, GlassPane> overlays = FXCollections.observableHashMap();
  private final ObservableSet<Node> nonBlockingOverlaysShown = FXCollections.observableSet();
  private final ObservableSet<Node> blockingOverlaysShown = FXCollections.observableSet();

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

  // Factories
  /**
   * The factories which are called when creating Tabs, Tiles and Pages of Tiles for the Views. They
   * require a module whose attributes are used to create the Nodes.
   */
  private final ObjectProperty<Callback<Workbench, Tab>> tabFactory =
      new SimpleObjectProperty<>(this, "tabFactory");
  private final ObjectProperty<Callback<Workbench, Tile>> tileFactory =
      new SimpleObjectProperty<>(this, "tileFactory");
  private final ObjectProperty<Callback<Workbench, Page>> pageFactory =
      new SimpleObjectProperty<>(this, "pageFactory");

  // Properties
  private final IntegerProperty modulesPerPage;

  Workbench(WorkbenchBuilder builder) {
    modulesPerPage = new SimpleIntegerProperty(builder.modulesPerPage);
    tabFactory.set(builder.tabFactory);
    tileFactory.set(builder.tileFactory);
    pageFactory.set(builder.pageFactory);
    initToolbarControls(builder);
    initNavigationDrawer(builder);
    initModules(builder.modules);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new WorkbenchSkin(this);
  }

  /**
   * Creates a builder for {@link Workbench}.
   *
   * @param modules which should be loaded for the application
   * @return builder object
   */
  public static WorkbenchBuilder builder(Module... modules) {
    return new WorkbenchBuilder(modules);
  }

  private void initToolbarControls(WorkbenchBuilder builder) {
    if (builder.toolbarControlsLeft != null) {
      toolbarControlsLeft.addAll(List.of(builder.toolbarControlsLeft));
    }

    if (builder.toolbarControlsRight != null) {
      toolbarControlsRight.addAll(List.of(builder.toolbarControlsRight));
    }
  }

  private void initNavigationDrawer(WorkbenchBuilder builder) {
    if (builder.navigationDrawerItems != null) {
      navigationDrawerItems.addAll(builder.navigationDrawerItems);
    }
    navigationDrawer = builder.navigationDrawerFactory.call(this);
  }

  private void initModules(Module... modules) {
    this.modules.addAll(modules);

    // handle changes of the active module
    activeModule.addListener((observable, oldModule, newModule) -> {
      LOGGER.trace("Module Listener - Old Module: " + oldModule);
      LOGGER.trace("Module Listener - New Module: " + newModule);
      if (oldModule != newModule) {
        boolean fromHomeScreen = oldModule == null;
        LOGGER.trace("Active Module Listener - Previous view home screen: " + fromHomeScreen);
        boolean fromDestroyed = !openModules.contains(oldModule);
        LOGGER.trace("Active Module Listener - Previous module destroyed: " + fromDestroyed);
        if (!fromHomeScreen && !fromDestroyed) {
          // switch from one module to another
          LOGGER.trace("Active Module Listener - Deactivating old module - " + oldModule);
          oldModule.deactivate();
        }
        boolean toHomeScreen = newModule == null;
        if (toHomeScreen) {
          // switch to home screen
          LOGGER.trace("Active Module Listener - Switched to home screen");
          activeModuleView.setValue(null);
          return;
        }
        if (!openModules.contains(newModule)) {
          // module has not been loaded yet
          LOGGER.trace("Active Module Listener - Initializing module - " + newModule);
          newModule.init(this);
          openModules.add(newModule);
        }
        LOGGER.trace("Active Module Listener - Activating module - " + newModule);
        activeModuleView.setValue(newModule.activate());
      }
    });
  }

  /**
   * Opens the {@code module} in a new tab, if it isn't initialized yet or else opens the tab of
   * it.
   *
   * @param module the module to be opened or null to go to the home view
   */
  public void openModule(Module module) {
    if (!modules.contains(module)) {
      throw new IllegalArgumentException(
          "Module was not passed in with the constructor of WorkbenchFxModel");
    }
    LOGGER.trace("openModule - set active module to " + module);
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
    Module oldActive = getActiveModule();
    Module newActive;
    if (oldActive != module) {
      // if we are not closing the currently active module, stay at the current
      newActive = oldActive;
    } else if (openModules.size() == 1) {
      // go to home screen
      newActive = null;
      LOGGER.trace("closeModule - Next active: Home Screen");
    } else if (i == 0) {
      // multiple modules open, leftmost is active
      newActive = openModules.get(i + 1);
      LOGGER.trace("closeModule - Next active: Next Module - " + newActive);
    } else {
      newActive = openModules.get(i - 1);
      LOGGER.trace("closeModule - Next active: Previous Module - " + newActive);
    }
    // attempt to destroy module
    if (!module.destroy()) {
      // module should or could not be destroyed
      LOGGER.trace("closeModule - Destroy: Fail - " + module);
      return false;
    } else {
      LOGGER.trace("closeModule - Destroy: Success - " + module);
      boolean removal = openModules.remove(module);
      LOGGER.trace("closeModule - Destroy, Removal successful: " + removal + " - " + module);
      LOGGER.trace("closeModule - Set active module to: " + newActive);
      activeModule.setValue(newActive);
      return removal;
    }
  }

  /**
   * Calculates the amount of pages of modules (rendered as tiles).
   *
   * @return amount of pages
   * @implNote Each page is filled up until there are as many tiles as {@code modulesPerPage}. This
   *           is repeated until all modules are rendered as tiles.
   */
  public int amountOfPages() {
    int amountOfModules = getModules().size();
    int modulesPerPage = getModulesPerPage();
    // if all pages are completely full
    if (amountOfModules % modulesPerPage == 0) {
      return amountOfModules / modulesPerPage;
    } else {
      // if the last page is not full, round up to the next page
      return amountOfModules / modulesPerPage + 1;
    }
  }

  public ObservableList<Module> getOpenModules() {
    return FXCollections.unmodifiableObservableList(openModules);
  }

  public ObservableList<Module> getModules() {
    return FXCollections.unmodifiableObservableList(modules);
  }

  /**
   * Adds the {@code module} to the home screen at runtime.
   *
   * @param module to be added
   * @return true if successful, false if already added
   */
  public boolean addModule(Module module) {
    if (modules.contains(module)) {
      return false;
    }
    return modules.add(module);
  }

  /**
   * Removes the {@code module} at runtime.
   * @param module to be removed
   * @return true if successful, false if not present
   */
  public boolean removeModule(Module module) {
    return modules.remove(module);
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

  public Node getNavigationDrawer() {
    return navigationDrawer;
  }

  /**
   * Removes a {@link Node} if one is in the {@code toolbarControlsLeft}.
   *
   * @param node the {@link Node} which should be removed
   * @return true if sucessful, false if not
   */
  public boolean removeToolbarControlLeft(Node node) {
    return toolbarControlsLeft.remove(node);
  }

  /**
   * Inserts the given {@code node} at the end of the left toolbar. If the left toolbar already
   * contains {@code node}, it will not be added.
   *
   * @param node to be added to the left toolbar
   * @return true if {@code node} was added to the left toolbar, false if not
   */
  public boolean addToolbarControlLeft(Node node) {
    return toolbarControlsLeft.add(node);
  }

  public ObservableSet<Node> getToolbarControlsLeft() {
    return FXCollections.unmodifiableObservableSet(toolbarControlsLeft);
  }

  /**
   * Removes a {@link Node} if one is in the {@code toolbarControlsRight}.
   *
   * @param node which should be removed
   * @return true if sucessful, false if not
   */
  public boolean removeToolbarControlRight(Node node) {
    return toolbarControlsRight.remove(node);
  }

  /**
   * Inserts a given {@code node} at the end of the right toolbar. If the right toolbar already
   * contains the {@code node}, it will not be added.
   *
   * @param node to be added to the right toolbar
   * @return true if {@code node} was added to the right toolbar, false if not
   */
  public boolean addToolbarControlRight(Node node) {
    return toolbarControlsRight.add(node);
  }

  public ObservableSet<Node> getToolbarControlsRight() {
    return FXCollections.unmodifiableObservableSet(toolbarControlsRight);
  }

  /**
   * Returns a map of all overlays, which have previously been opened, with their corresponding
   * {@link GlassPane}.
   */
  public ObservableMap<Node, GlassPane> getOverlays() {
    return FXCollections.unmodifiableObservableMap(overlays);
  }

  /**
   * Shows the {@code overlay} on top of the view, with a {@link GlassPane} in the background.
   *
   * @param overlay  to be shown
   * @param blocking If false (non-blocking), clicking outside of the {@code overlay} will cause it
   *                 to get hidden, together with its {@link GlassPane}. If true (blocking),
   *                 clicking outside of the {@code overlay} will not do anything. The {@code
   *                 overlay} itself must call {@link Workbench#hideOverlay(Node, boolean)} to hide
   *                 it.
   */
  public boolean showOverlay(Node overlay, boolean blocking) {
    LOGGER.trace("showOverlay");
    if (!overlays.containsKey(overlay)) {
      overlays.put(overlay, new GlassPane());
    }
    if (blocking) {
      return blockingOverlaysShown.add(overlay);
    } else {
      return nonBlockingOverlaysShown.add(overlay);
    }
  }

  /**
   * Hides the {@code overlay} together with its {@link GlassPane}, which has previously been shown
   * using {@link Workbench#showOverlay(Node, boolean)}.
   *
   * @param overlay  to be hidden
   * @implNote As the method's name implies, this will only <b>hide</b> the {@code overlay}, not
   *           remove it from the scene graph entirely.
   *           If keeping the {@code overlay} loaded hidden in the scene graph is not possible due
   *           to performance reasons, call {@link Workbench#clearOverlays()} after this method.
   */
  public boolean hideOverlay(Node overlay) {
    LOGGER.trace("hideOverlay");
    if (blockingOverlaysShown.contains(overlay)) {
      return blockingOverlaysShown.remove(overlay);
    } else {
      return nonBlockingOverlaysShown.remove(overlay);
    }
  }

  /**
   * Removes all previously loaded overlays from the scene graph including all references to them,
   * in order to free up memory.
   */
  public void clearOverlays() {
    LOGGER.trace("clearOverlays");
    nonBlockingOverlaysShown.clear();
    blockingOverlaysShown.clear();
    overlays.clear();
  }

  public void showNavigationDrawer() {
    showOverlay(navigationDrawer, false);
  }

  public void hideNavigationDrawer() {
    hideOverlay(navigationDrawer, false);
  }

  public ObservableList<MenuItem> getNavigationDrawerItems() {
    return FXCollections.unmodifiableObservableList(navigationDrawerItems);
  }

  public void addNavigationDrawerItems(MenuItem... menuItems) {
    navigationDrawerItems.addAll(menuItems);
  }

  public void removeNavigationDrawerItems(MenuItem... menuItems) {
    navigationDrawerItems.removeAll(menuItems);
  }

  public ObservableSet<Node> getNonBlockingOverlaysShown() {
    return FXCollections.unmodifiableObservableSet(nonBlockingOverlaysShown);
  }

  public ObservableSet<Node> getBlockingOverlaysShown() {
    return FXCollections.unmodifiableObservableSet(blockingOverlaysShown);
  }

  public int getModulesPerPage() {
    return modulesPerPage.get();
  }

  public IntegerProperty modulesPerPageProperty() {
    return modulesPerPage;
  }

  public void setModulesPerPage(int modulesPerPage) {
    this.modulesPerPage.set(modulesPerPage);
  }

  public Callback<Workbench, Tab> getTabFactory() {
    return tabFactory.get();
  }

  public ObjectProperty<Callback<Workbench, Tab>> tabFactoryProperty() {
    return tabFactory;
  }

  public void setTabFactory(Callback<Workbench, Tab> tabFactory) {
    this.tabFactory.set(tabFactory);
  }

  public Callback<Workbench, Tile> getTileFactory() {
    return tileFactory.get();
  }

  public ObjectProperty<Callback<Workbench, Tile>> tileFactoryProperty() {
    return tileFactory;
  }

  public void setTileFactory(Callback<Workbench, Tile> tileFactory) {
    this.tileFactory.set(tileFactory);
  }

  public Callback<Workbench, Page> getPageFactory() {
    return pageFactory.get();
  }

  public ObjectProperty<Callback<Workbench, Page>> pageFactoryProperty() {
    return pageFactory;
  }

  public void setPageFactory(Callback<Workbench, Page> pageFactory) {
    this.pageFactory.set(pageFactory);
  }

  @Override
  public String getUserAgentStylesheet() {
    return Workbench.class.getResource("css/main.css").toExternalForm();
  }
}
