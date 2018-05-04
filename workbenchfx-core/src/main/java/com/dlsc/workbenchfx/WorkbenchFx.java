package com.dlsc.workbenchfx;

import static impl.org.controlsfx.ReflectionUtils.addUserAgentStylesheet;

import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.overlay.Overlay;
import com.dlsc.workbenchfx.view.ContentPresenter;
import com.dlsc.workbenchfx.view.ContentView;
import com.dlsc.workbenchfx.view.HomePresenter;
import com.dlsc.workbenchfx.view.HomeView;
import com.dlsc.workbenchfx.view.ToolbarPresenter;
import com.dlsc.workbenchfx.view.ToolbarView;
import com.dlsc.workbenchfx.view.WorkbenchFxPresenter;
import com.dlsc.workbenchfx.view.WorkbenchFxView;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import com.google.common.collect.HashBiMap;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the main WorkbenchFX class.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class WorkbenchFx extends StackPane {
  private static final Logger LOGGER = LogManager.getLogger(WorkbenchFx.class.getName());

  public static final String STYLE_CLASS_ACTIVE_TAB = "active-tab";

  // Views
  private ToolbarView toolbarView;
  private ToolbarPresenter toolbarPresenter;

  private HomeView homeView;
  private HomePresenter homePresenter;

  private ContentView contentView;
  private ContentPresenter contentPresenter;

  private WorkbenchFxView workbenchFxView;
  private WorkbenchFxPresenter workbenchFxPresenter;

  // Custom Controls
  private Overlay navigationDrawer;

  // Lists
  private final ObservableList<Node> toolbarControls = FXCollections.observableArrayList();
  private final ObservableList<MenuItem> navigationDrawerItems =
      FXCollections.observableArrayList();

  /**
   * Map containing a linked list of all overlays which have been loaded onto the scene graph, with
   * their corresponding {@link GlassPane}.
   */
  private final ObservableMap<Overlay, GlassPane> overlays = FXCollections.observableMap(HashBiMap.create(new LinkedHashMap<>()));

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
   * The factories which are called when creating Tabs, Tiles and Pages of Tiles for the Views.
   * They require a module whose attributes are used to create the Nodes.
   */
  private final ObjectProperty<BiFunction<WorkbenchFx, Module, Node>> tabFactory =
      new SimpleObjectProperty<>(this, "tabFactory");
  private final ObjectProperty<BiFunction<WorkbenchFx, Module, Node>> tileFactory =
      new SimpleObjectProperty<>(this, "tileFactory");
  private final ObjectProperty<BiFunction<WorkbenchFx, Integer, Node>> pageFactory =
      new SimpleObjectProperty<>(this, "pageFactory");

  // Properties
  public final int modulesPerPage;
  private final BooleanProperty navigationDrawerShown = new SimpleBooleanProperty(false);

  WorkbenchFx(WorkbenchFxBuilder builder) {
    modulesPerPage = builder.modulesPerPage;
    tabFactory.set(builder.tabFactory);
    tileFactory.set(builder.tileFactory);
    pageFactory.set(builder.pageFactory);
    initToolbarControls(builder);
    initNavigationDrawer(builder);
    initOverlays(builder);
    initModules(builder.modules);
    initViews();
    getChildren().add(workbenchFxView);
    Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
    addUserAgentStylesheet(WorkbenchFx.class.getResource("css/main.css").toExternalForm());
  }

  /**
   * Creates a builder for {@link WorkbenchFx}.
   *
   * @param modules which should be loaded for the application
   * @return builder object
   */
  public static WorkbenchFxBuilder builder(Module... modules) {
    return new WorkbenchFxBuilder(modules);
  }

  private void initToolbarControls(WorkbenchFxBuilder builder) {
    if (builder.toolbarControls != null) {
      toolbarControls.addAll(builder.toolbarControls);
    }
  }

  private void initNavigationDrawer(WorkbenchFxBuilder builder) {
    if (builder.navigationDrawerItems != null) {
      navigationDrawerItems.addAll(builder.navigationDrawerItems);
    }
    navigationDrawer = builder.navigationDrawerFactory.call(this);
    addOverlay(navigationDrawer);
  }

  private void initOverlays(WorkbenchFxBuilder builder) {
    if (Objects.isNull(builder.overlays)) {
      return;
    }
    Arrays.stream(builder.overlays).forEachOrdered(this::addOverlay);
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

  private void initViews() {
    toolbarView = new ToolbarView();
    toolbarPresenter = new ToolbarPresenter(this, toolbarView);

    homeView = new HomeView();
    homePresenter = new HomePresenter(this, homeView);

    contentView = new ContentView();
    contentPresenter = new ContentPresenter(this, contentView);

    workbenchFxView = new WorkbenchFxView(toolbarView, homeView, contentView);
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
   * @implNote Each page is filled up until there are as many tiles as {@code modulesPerPage}.
   *           This is repeated until all modules are rendered as tiles.
   */
  public int amountOfPages() {
    int amountOfModules = getModules().size();
    // if all pages are completely full
    if (amountOfModules % modulesPerPage == 0) {
      return amountOfModules / modulesPerPage;
    } else {
      // if the last page is not full, round up to the next page
      return amountOfModules / modulesPerPage + 1;
    }
  }

  /**
   * Generates a new Node which is then used as a Tab.
   * Using the given {@link Module}, it calls the {@code tabFactory} which generates the Tab.
   *
   * @param module the module for which the Tab should be created
   * @return a corresponding Tab which is created from the {@code tabFactory}
   */
  public Node getTab(Module module) {
    return tabFactory.get().apply(this, module);
  }

  /**
   * Generates a new Node which is then used as a Tile.
   * Using the given {@link Module}, it calls the {@code tileFactory} which generates the Tile.
   *
   * @param module the module for which the Tile should be created
   * @return a corresponding Tile which contains the values of the module
   */
  public Node getTile(Module module) {
    return tileFactory.get().apply(this, module);
  }

  /**
   * Generates a new Node which is then used as a page for the tiles on the home screen.
   * Using the given {@code pageIndex}, it calls the {@code pageFactory} which generates the page.
   *
   * @param pageIndex the page index for which the page should be created
   * @return a corresponding page
   */
  public Node getPage(int pageIndex) {
    return pageFactory.get().apply(this, pageIndex);
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

  public Overlay getNavigationDrawer() {
    return navigationDrawer;
  }

  /**
   * Removes a {@link Node} if one is in the {@code toolbarControls}.
   *
   * @param node the {@link Node} which should be removed
   * @return true if sucessful, false if not
   */
  public boolean removeToolbarControl(Node node) {
    return toolbarControls.remove(node);
  }

  /**
   * Inserts a given {@link Node} at the end of the {@code toolbarControls}.
   * If the {@code toolbarControls} already contains the {@link Node} it will not be added.
   *
   * @param node the {@link Node} to be added to the {@code toolbarControls}
   * @return true if {@code toolbarControls} was changed, false if not
   */
  public boolean addToolbarControl(Node node) {
    if (!toolbarControls.contains(node)) {
      toolbarControls.add(node);
      return true;
    }
    return false;
  }

  public ObservableList<Node> getToolbarControls() {
    return FXCollections.unmodifiableObservableList(toolbarControls);
  }

  /**
   * Returns the list of all overlays. TODO
   */
  public ObservableMap<Overlay, GlassPane> getOverlays() {
    return FXCollections.unmodifiableObservableMap(overlays);
  }

  /**
   * Loads an overlay into the scene graph hidden, to be shown using TODO.
   *
   * @param overlay to be loaded into the scene graph
   * @implNote Preferably, use the builder method TODO
   *           and load all of the overlays initially. Only use this method if keeping the overlay
   *           loaded in the background is not possible due to performance reasons!
   */
  public void addOverlay(Overlay overlay) {
    LOGGER.trace("addOverlay");
    overlay.init(this);
    overlays.put(overlay, new GlassPane());
  }

  /**
   * Removes an overlay from the scene graph, which has previously been loaded either using
   * {@link WorkbenchFx#addOverlay(Overlay)} or TODO.
   *
   * @param overlay to be removed from the scene graph
   * @implNote Preferably, don't use this method to remove the overlays from the scene graph, but
   *           rather use TODO. Only use this method if
   *           keeping the overlay loaded in the background is not possible due to performance
   */
  public void removeOverlay(Overlay overlay) {
    LOGGER.trace("removeOverlay");
    overlays.remove(overlay);
  }

  public boolean getNavigationDrawerShown() {
    return navigationDrawerShown.get();
  }

  public BooleanProperty navigationDrawerShownProperty() {
    return navigationDrawerShown;
  }

  public void setNavigationDrawerShown(boolean navigationDrawerShown) {
    this.navigationDrawerShown.set(navigationDrawerShown);
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
}
