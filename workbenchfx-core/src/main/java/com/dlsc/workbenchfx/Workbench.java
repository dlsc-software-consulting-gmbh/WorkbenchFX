package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchDialog.Type;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.model.WorkbenchOverlay;
import com.dlsc.workbenchfx.view.WorkbenchPresenter;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import com.dlsc.workbenchfx.view.controls.module.Page;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import com.google.common.collect.Range;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains all the model logic for the workbench.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class Workbench extends Control {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Workbench.class.getName());

  // Constants
  private static final int MAX_PERCENT = 100;

  /**
   * Duration of animations according to material design guidelines.
   * @see <a href="https://material.io/design/motion/speed.html#duration">
   * Speed - Material Design</a>
   */
  private static final int ANIMATION_DURATION_DRAWER_OPEN = 250;
  private static final int ANIMATION_DURATION_DRAWER_CLOSE = 200;

  // Default values
  private static final Callback<Workbench, Tab> DEFAULT_TAB_FACTORY = Tab::new;
  private static final Callback<Workbench, Tile> DEFAULT_TILE_FACTORY = Tile::new;
  private static final Callback<Workbench, Page> DEFAULT_PAGE_FACTORY = Page::new;
  private static final int DEFAULT_MODULES_PER_PAGE = 6;
  private static final NavigationDrawer DEFAULT_NAVIGATION_DRAWER = new NavigationDrawer();

  // Custom Controls
  private final ObjectProperty<NavigationDrawer> navigationDrawer =
      new SimpleObjectProperty<>(this, "navigationDrawer", DEFAULT_NAVIGATION_DRAWER);

  // Lists
  private final ObservableList<ToolbarItem> toolbarControlsRight =
      FXCollections.observableArrayList();
  private final ObservableList<ToolbarItem> toolbarControlsLeft =
      FXCollections.observableArrayList();
  private final ObservableList<MenuItem> navigationDrawerItems =
      FXCollections.observableArrayList();

  /**
   * Map containing all overlays which have been loaded into the scene graph, with their
   * corresponding model object {@link WorkbenchOverlay}.
   */
  private final ObservableMap<Region, WorkbenchOverlay> overlays =
      FXCollections.observableHashMap();

  private final ObservableList<Region> nonBlockingOverlaysShown =
      FXCollections.observableArrayList();
  private final ObservableList<Region> blockingOverlaysShown =
      FXCollections.observableArrayList();

  private final ObjectProperty<Region> drawerShown =
      new SimpleObjectProperty<>(this, "drawerShown");
  private final ObjectProperty<Side> drawerSideShown =
      new SimpleObjectProperty<>(this, "drawerSideShown");

  // Modules
  /**
   * List of all modules.
   */
  private final ListProperty<WorkbenchModule> modules = new SimpleListProperty<>(this, "modules",
      FXCollections.observableArrayList());

  /**
   * List of all currently open modules. Open modules are being displayed as open tabs in the
   * application.
   */
  private final ListProperty<WorkbenchModule> openModules = new SimpleListProperty<>(this,
      "modules",
      FXCollections.observableArrayList());

  /**
   * Will close the module without calling {@link WorkbenchModule#destroy()} if the corresponding
   * {@link CompletableFuture} is completed. If the stage was closed and {@code false} was returned
   * on {@link WorkbenchModule#destroy()}, it will also trigger {@link
   * Stage#setOnCloseRequest(EventHandler)}. Is <b>always</b> completed with {@code true}. This way,
   * there is no need to differentiate whether it was completed with {@code true} or {@code false}.
   */
  private final Map<WorkbenchModule, CompletableFuture<Boolean>> moduleCloseableMap =
      new HashMap<>();

  /**
   * Currently active module. Active module is the module, which is currently being displayed in the
   * view. When the home screen is being displayed, {@code activeModule} and {@code
   * activeModuleView} are null.
   */
  private final ObjectProperty<WorkbenchModule> activeModule =
      new SimpleObjectProperty<>(this, "activeModule");
  private final ObjectProperty<Node> activeModuleView =
      new SimpleObjectProperty<>(this, "activeModuleView");

  // Factories
  /**
   * The factories which are called when creating Tabs, Tiles and Pages of Tiles for the Views. They
   * require a module whose attributes are used to create the Nodes.
   */
  private final ObjectProperty<Callback<Workbench, Tab>> tabFactory =
      new SimpleObjectProperty<>(this, "tabFactory", DEFAULT_TAB_FACTORY);
  private final ObjectProperty<Callback<Workbench, Tile>> tileFactory =
      new SimpleObjectProperty<>(this, "tileFactory", DEFAULT_TILE_FACTORY);
  private final ObjectProperty<Callback<Workbench, Page>> pageFactory =
      new SimpleObjectProperty<>(this, "pageFactory", DEFAULT_PAGE_FACTORY);

  // Properties
  private final IntegerProperty modulesPerPage =
      new SimpleIntegerProperty(this, "modulesPerPage", DEFAULT_MODULES_PER_PAGE);
  private final IntegerProperty amountOfPages = new SimpleIntegerProperty(this, "amountOfPages");

  // Builder
  /**
   * Creates a builder for {@link Workbench}.
   *
   * @param modules which should be loaded for the application
   * @return builder object
   */
  public static WorkbenchBuilder builder(WorkbenchModule... modules) {
    return new WorkbenchBuilder(modules);
  }

  public static final class WorkbenchBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkbenchBuilder.class.getName());

    // Required parameters
    private WorkbenchModule[] modules;

    // Optional parameters - initialized to default values
    private int modulesPerPage = DEFAULT_MODULES_PER_PAGE;

    private Callback<Workbench, Tab> tabFactory = DEFAULT_TAB_FACTORY;

    private Callback<Workbench, Tile> tileFactory = DEFAULT_TILE_FACTORY;

    private Callback<Workbench, Page> pageFactory = DEFAULT_PAGE_FACTORY;

    private ToolbarItem[] toolbarControlsRight;
    private ToolbarItem[] toolbarControlsLeft;

    private NavigationDrawer navigationDrawer = DEFAULT_NAVIGATION_DRAWER;

    private MenuItem[] navigationDrawerItems;

    private WorkbenchBuilder(WorkbenchModule... modules) {
      this.modules = modules;
    }

    /**
     * Defines how many modules should be shown per page on the home screen.
     *
     * @param modulesPerPage amount of modules to be shown per page
     * @return builder for chaining
     */
    public final WorkbenchBuilder modulesPerPage(int modulesPerPage) {
      this.modulesPerPage = modulesPerPage;
      return this;
    }

    /**
     * Defines how {@link Tab} should be created to be used as tabs in the view.
     *
     * @param tabFactory to be used to create the {@link Tab}
     * @return builder for chaining
     * @implNote Use this to replace the control which is used for the tab with your own
     *           implementation.
     */
    public final WorkbenchBuilder tabFactory(Callback<Workbench, Tab> tabFactory) {
      this.tabFactory = tabFactory;
      return this;
    }

    /**
     * Defines how {@link Tab} should be created to be used as tiles in the home screen.
     *
     * @param tileFactory to be used to create the {@link Tile}
     * @return builder for chaining
     * @implNote Use this to replace the control which is used for the tiles with your own
     *           implementation.
     */
    public final WorkbenchBuilder tileFactory(Callback<Workbench, Tile> tileFactory) {
      this.tileFactory = tileFactory;
      return this;
    }

    /**
     * Defines how a {@link Page} with tiles of {@link WorkbenchModule}s should be created.
     *
     * @param pageFactory to be used to create the {@link Page} for the tiles
     * @return builder for chaining
     * @implNote Use this to replace the page which is used in the home screen to display tiles
     *           of the modules with your own implementation.
     */
    public final WorkbenchBuilder pageFactory(Callback<Workbench, Page> pageFactory) {
      this.pageFactory = pageFactory;
      return this;
    }

    /**
     * Defines which navigation drawer should be shown.
     *
     * @param navigationDrawer to be shown as the navigation drawer
     * @return builder for chaining
     * @implNote Use this to replace the navigation drawer, which is displayed when pressing the
     *           menu icon, with your own implementation. To access the {@link MenuItem}s, use
     *           {@link Workbench#getNavigationDrawerItems()}.
     */
    public final WorkbenchBuilder navigationDrawer(NavigationDrawer navigationDrawer) {
      this.navigationDrawer = navigationDrawer;
      return this;
    }

    /**
     * Defines the {@link MenuItem}s, which will be rendered using the respective {@code
     * navigationDrawerFactory}.
     *
     * @param navigationDrawerItems the {@link MenuItem}s to display or null, if there should be no
     *                              menu
     * @return builder for chaining
     * @implNote the menu button will be hidden, if null is passed to {@code navigationDrawerItems}
     */
    public final WorkbenchBuilder navigationDrawerItems(MenuItem... navigationDrawerItems) {
      this.navigationDrawerItems = navigationDrawerItems;
      return this;
    }

    /**
     * Defines the Controls which are placed on top-left of the Toolbar.
     *
     * @param toolbarControlsLeft the {@link ToolbarItem}s which will be added to the Toolbar
     * @return the updated {@link WorkbenchBuilder}
     */
    public final WorkbenchBuilder toolbarLeft(ToolbarItem... toolbarControlsLeft) {
      this.toolbarControlsLeft = toolbarControlsLeft;
      return this;
    }

    /**
     * Defines the Controls which are placed on top-right of the Toolbar.
     *
     * @param toolbarControlsRight the {@link ToolbarItem}s which will be added to the Toolbar
     * @return the updated {@link WorkbenchBuilder}
     */
    public final WorkbenchBuilder toolbarRight(ToolbarItem... toolbarControlsRight) {
      this.toolbarControlsRight = toolbarControlsRight;
      return this;
    }

    /**
     * Builds and fully initializes a {@link Workbench} object.
     *
     * @return the {@link Workbench} object
     */
    public final Workbench build() {
      return new Workbench(this);
    }
  }

  /**
   * Default constructor for use with Scene Builder.
   * For use without FXML, use {@link Workbench#builder(WorkbenchModule...)} instead.
   */
  public Workbench() {
    initBindings();
    initListeners();
    initNavigationDrawer(getNavigationDrawer());
    setupCleanup();
    getStylesheets().add(Workbench.class.getResource("css/context-menu.css").toExternalForm());
    getStyleClass().add("workbench");
  }

  /**
   * Constructor for WorkbenchFX by using the {@link WorkbenchBuilder}.
   *
   * @param builder to use for the setup
   */
  private Workbench(WorkbenchBuilder builder) {
    this();
    setModulesPerPage(builder.modulesPerPage);
    initFactories(builder);
    initToolbarControls(builder);
    initNavigationDrawer(builder);
    initModules(builder);
  }

  private void initFactories(WorkbenchBuilder builder) {
    tabFactory.set(builder.tabFactory);
    tileFactory.set(builder.tileFactory);
    pageFactory.set(builder.pageFactory);
  }

  private void initBindings() {
    amountOfPages.bind(
        Bindings.createIntegerBinding(
            this::calculateAmountOfPages, modulesPerPageProperty(), getModules()
        )
    );
  }

  @Override
  protected final Skin<?> createDefaultSkin() {
    return new WorkbenchSkin(this);
  }

  private void initToolbarControls(WorkbenchBuilder builder) {
    if (builder.toolbarControlsLeft != null) {
      toolbarControlsLeft.addAll(Arrays.asList(builder.toolbarControlsLeft));
    }

    if (builder.toolbarControlsRight != null) {
      toolbarControlsRight.addAll(Arrays.asList(builder.toolbarControlsRight));
    }
  }

  private void initNavigationDrawer(WorkbenchBuilder builder) {
    if (builder.navigationDrawerItems != null) {
      navigationDrawerItems.addAll(builder.navigationDrawerItems);
    }
    initNavigationDrawer(builder.navigationDrawer);
  }

  private void initNavigationDrawer(NavigationDrawer navigationDrawer) {
    setNavigationDrawer(navigationDrawer);
    navigationDrawer.setWorkbench(this);
  }

  private void initModules(WorkbenchBuilder builder) {
    WorkbenchModule[] modules = builder.modules;

    this.modules.addAll(modules);
  }

  private void initListeners() {
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
          resetModuleCloseable(newModule); // initialize closing on call to #close()
          openModules.add(newModule);
        }
        LOGGER.trace("Active Module Listener - Activating module - " + newModule);
        activeModuleView.setValue(newModule.activate());
      }
    });

    // handle drawer changes
    drawerShown.addListener((observable, oldDrawer, newDrawer) -> {
      if (!Objects.isNull(oldDrawer)) {
        hideOverlay(oldDrawer);
      }
      if (!Objects.isNull(newDrawer)) {
        showOverlay(newDrawer, false, drawerSideShown.get());
      }
    });


    // when control of navigation drawer changes, pass in the workbench object
    navigationDrawerProperty().addListener((observable, oldControl, newControl) -> {
      LOGGER.trace("NavigationDrawer has been set");
      if (!Objects.isNull(newControl)) {
        LOGGER.trace("NavigationDrawer - Setting Workbench");
        newControl.setWorkbench(this);
      }
    });
  }

  private void setupCleanup() {
    Platform.runLater(() -> {
      Scene scene = getScene();
      // if there is no scene, don't cause NPE by calling "getWindow()" on null
      if (Objects.isNull(scene)) {
        // should only be thrown in tests with mocked views
        LOGGER.error("setupCleanup - Scene could not be found! setOnCloseRequest was not set");
        return;
      }

      Stage stage = (Stage) getScene().getWindow();
      // when application is closed, destroy all modules
      stage.setOnCloseRequest(event -> {
        LOGGER.trace("Stage was requested to be closed");
        event.consume(); // we need to perform some cleanup actions first

        // close all open modules until one returns false
        while (!getOpenModules().isEmpty()) {
          WorkbenchModule openModule = getOpenModules().get(0);
          if (!closeModule(openModule)) {
            LOGGER.trace("Module " + openModule + " could not be closed yet");

            // once module is ready to be closed, start stage closing process over again
            getModuleCloseable(openModule).thenRun(() -> {
              LOGGER.trace("moduleCloseable - Stage - thenRun triggered: " + openModule);
              LOGGER.trace(openModule + " restarted stage closing process");
              // re-start closing process, in case other modules are blocking the closing process
              stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            });
            LOGGER.trace("moduleCloseable - Stage - thenRun set: " + openModule);

            break; // interrupt closing until the interrupting module has been safely closed
          }
        }

        if (getOpenModules().isEmpty()) {
          LOGGER.trace("All modules could be closed successfully, closing stage");
          stage.close();
        }
      });
    });
  }

  /**
   * Opens the {@code module} in a new tab, if it isn't initialized yet or else opens the tab of
   * it.
   *
   * @param module the module to be opened or null to go to the home view
   */
  public final void openModule(WorkbenchModule module) {
    if (!modules.contains(module)) {
      throw new IllegalArgumentException(
          "Module has not been loaded yet");
    }
    LOGGER.trace("openModule - set active module to " + module);
    activeModule.setValue(module);
  }

  /**
   * Goes back to the AddModulePage screen where the user can choose between modules.
   */
  public final void openAddModulePage() {
    activeModule.setValue(null);
  }

  /**
   * Closes the {@code module}.
   *
   * @param module to be closed
   * @return true if closing was successful
   */
  public final boolean closeModule(WorkbenchModule module) {
    LOGGER.trace("closeModule - " + module);
    LOGGER.trace("closeModule - List of open modules: " + openModules);
    Objects.requireNonNull(module);
    int i = openModules.indexOf(module);
    if (i == -1) {
      throw new IllegalArgumentException("Module has not been opened yet.");
    }
    // set new active module
    WorkbenchModule oldActive = getActiveModule();
    WorkbenchModule newActive;
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
    // if the currently active module is the one that is being closed, deactivate first
    if (oldActive == module) {
      LOGGER.trace("closeModule - " + module + " was deactivated");
      module.deactivate();
    }
    /*
      If module has previously been closed and can now safely be closed, calling destroy() is not
      necessary anymore, simply remove the module
      If this module is being closed the first time or cannot be safely closed yet, attempt to
      destroy module.
      Note: destroy() will not be called if moduleCloseable was completed with true!
     */
    if (getModuleCloseable(module).getNow(false) || module.destroy()) {
      LOGGER.trace("closeModule - Destroy: Success - " + module);
      boolean removal = openModules.remove(module);
      moduleCloseableMap.remove(module);
      LOGGER.trace("closeModule - Destroy, Removal successful: " + removal + " - " + module);
      if (oldActive != newActive) {
        // only log if the active module has been changed
        LOGGER.trace("closeModule - Set active module to: " + newActive);
      }
      activeModule.setValue(newActive);
      return removal;
    } else {
      /*
        If moduleCloseable wasn't completed yet but closeModule was called, there are two cases:
        1. The stage is calling closeModule() => since thenRun will be set on moduleCloseable after
           closeModule() returns "false", we need to reset moduleCloseable so that repeating stage
           closes without completing moduleClosable won't lead to multiple thenRun actions being
           layered with each stage close.
        2. The tab is being closed, calling closeModule() => if there was a stage close beforehand
           (and thus a thenRun from the stage closing process is still active) we need to
           reset moduleCloseable so that the stage closing process will not be triggered again.
       */
      resetModuleCloseable(module);
      // module should or could not be destroyed
      LOGGER.trace("closeModule - Destroy: Fail - " + module);
      // if the module that has failed to be destroyed is already open, activate it again
      if (getActiveModule() == module) {
        module.activate();
      }
      openModule(module); // set focus to new module
      return false;
    }
  }

  /**
   * Calculates the amount of pages of modules (rendered as tiles).
   *
   * @return amount of pages
   * @implNote Each page is filled up until there are as many tiles as {@code modulesPerPage}.
   *           This is repeated until all modules are rendered as tiles.
   */
  private int calculateAmountOfPages() {
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

  /**
   * Completes the {@code moduleCloseable} of the {@code module}.
   * Results in the module getting closed without calling {@link WorkbenchModule#destroy()}
   * beforehand. If the stage was closed and calling {@link WorkbenchModule#destroy()} returned
   * {@code false}, calling this method will also cause the stage closing process to get continued.
   *
   * @param module whose {@code moduleCloseable} should be completed
   */
  public final void completeModuleCloseable(WorkbenchModule module) {
    getModuleCloseable(module).complete(true);
  }

  private void resetModuleCloseable(WorkbenchModule module) {
    LOGGER.trace("moduleCloseable - Cleared future: " + this);
    CompletableFuture<Boolean> moduleCloseable = new CompletableFuture<>();
    moduleCloseableMap.put(module, moduleCloseable);
    LOGGER.trace("moduleCloseable - thenRun set: " + this);
    moduleCloseable.thenRun(() -> {
      LOGGER.trace("moduleCloseable -  thenRun triggered: " + this);
      closeModule(module);
    });
  }

  /**
   * Shows the {@code overlay} on top of the view, with a {@link GlassPane} in the background.
   *
   * @param overlay  to be shown
   * @param blocking If false (non-blocking), clicking outside of the {@code overlay} will cause it
   *                 to get hidden, together with its {@link GlassPane}. If true (blocking),
   *                 clicking outside of the {@code overlay} will not do anything. The {@code
   *                 overlay} itself must call {@link Workbench#hideOverlay(Region)} to hide it.
   * @return true if the overlay is not being shown already
   */
  public final boolean showOverlay(Region overlay, boolean blocking) {
    LOGGER.trace("showOverlay");
    if (!overlays.containsKey(overlay)) {
      overlays.put(overlay, new WorkbenchOverlay(overlay, new GlassPane()));
    }
    // To prevent showing the same overlay twice
    if (blockingOverlaysShown.contains(overlay) || nonBlockingOverlaysShown.contains(overlay)) {
      return false;
    }
    if (blocking) {
      LOGGER.trace("showOverlay - blocking");
      return blockingOverlaysShown.add(overlay);
    } else {
      LOGGER.trace("showOverlay - non-blocking");
      return nonBlockingOverlaysShown.add(overlay);
    }
  }

  /**
   * Shows the {@code overlay} on top of the view, with a {@link GlassPane} in the background.
   * The overlay will be shown and hidden with an animation, sliding the overlay in and out
   * from the defined {@code side}.
   *
   * @param overlay to be shown
   * @param blocking If false (non-blocking), clicking outside of the {@code overlay} will cause it
   *                 to get hidden, together with its {@link GlassPane}. If true (blocking),
   *                 clicking outside of the {@code overlay} will not do anything. The {@code
   *                 overlay} itself must call {@link Workbench#hideOverlay(Region)} to hide it.
   * @param side from which the {@code overlay} should be slid when the overlay is being shown or
   *             hidden
   * @return true if the overlay is not being shown already
   */
  private boolean showOverlay(Region overlay, boolean blocking, Side side) {
    LOGGER.trace("showOverlay - animated");
    if (!overlays.containsKey(overlay)) {
      overlays.put(overlay,
          new WorkbenchOverlay(overlay, new GlassPane(), slideIn(overlay), slideOut(overlay))
      );
      addInitialAnimationHandler(overlays.get(overlay), side);
    }
    return showOverlay(overlay, blocking);
  }

  /**
   * Handles the initial animation of an overlay.
   *
   * <p>An overlay will have a default size of {@code 0}, when it is <b>first being shown</b> by
   * {@link WorkbenchPresenter#showOverlay(Region, boolean)}, because it has not been added to the
   * scene graph or a layout pass has not been performed yet. This means the animation won't be
   * played by {@link WorkbenchPresenter#showOverlay(Region, boolean)} as well.<br>
   * For this reason, we wait for the {@link WorkbenchOverlay} to be initialized and then initially
   * set the coordinates of the overlay to be outside of the {@link Scene}, followed by playing the
   * initial starting animation.<br>
   * Any subsequent calls which show this {@code workbenchOverlay} again will <b>not</b> cause this
   * to trigger again, as the {@link Event} of {@link WorkbenchOverlay#onInitializedProperty()}
   * will only be fired once, since calling {@link Workbench#hideOverlay(Region)} only makes the
   * overlays not visible, which means the nodes remain with their size already initialized in the
   * scene graph.
   *
   * @param workbenchOverlay for which to prepare the initial animation handler for
   * @param side from which the sliding animation should originate
   */
  private void addInitialAnimationHandler(WorkbenchOverlay workbenchOverlay, Side side) {
    Region overlay = workbenchOverlay.getOverlay();
    // prepare values for setting the listener
    ReadOnlyDoubleProperty size =
        side.isVertical() ? overlay.widthProperty() : overlay.heightProperty();
    //                       LEFT or RIGHT side          TOP or BOTTOM side

    // make sure this code only gets run the first time the overlay has been shown and
    // rendered in the scene graph, to ensure the overlay has a size for the calculations
    workbenchOverlay.setOnInitialized(event -> {
      // prepare variables
      TranslateTransition start = workbenchOverlay.getAnimationStart();
      TranslateTransition end = workbenchOverlay.getAnimationEnd();
      DoubleExpression hiddenCoordinate = DoubleBinding.doubleExpression(size);
      if (Side.LEFT.equals(side) || Side.TOP.equals(side)) {
        hiddenCoordinate = hiddenCoordinate.negate(); // make coordinates in hidden state negative
      }

      if (side.isVertical()) { // LEFT or RIGHT => X
        overlay.setTranslateX(hiddenCoordinate.get()); // initial position
        start.setToX(0);
        if (!end.toXProperty().isBound()) {
          end.toXProperty().bind(hiddenCoordinate);
        }
      }
      if (side.isHorizontal()) { // TOP or BOTTOM => Y
        overlay.setTranslateY(hiddenCoordinate.get()); // initial position
        start.setToY(0);
        if (!end.toYProperty().isBound()) {
          end.toYProperty().bind(hiddenCoordinate);
        }
      }

      start.play();
    });
  }

  private TranslateTransition slideIn(Region overlay) {
    TranslateTransition open = new TranslateTransition(
        new Duration(ANIMATION_DURATION_DRAWER_OPEN), overlay);
    return open;
  }

  private TranslateTransition slideOut(Region overlay) {
    TranslateTransition close = new TranslateTransition(
        new Duration(ANIMATION_DURATION_DRAWER_CLOSE), overlay);
    close.setOnFinished(event -> {
      overlay.setVisible(false);
      LOGGER.trace(
          "Overlay LayoutX: " + overlay.getLayoutX() + " TranslateX: " + overlay.getTranslateX());
    });
    return close;
  }

  /**
   * Hides the {@code overlay} together with its {@link GlassPane}, which has previously been shown
   * using {@link Workbench#showOverlay(Region, boolean)}.
   *
   * @param overlay to be hidden
   * @return true if the overlay was showing and is now hidden
   * @implNote As the method's name implies, this will only <b>hide</b> the {@code overlay}, not
   *           remove it from the scene graph entirely. If keeping the {@code overlay} loaded hidden
   *           in the scene graph is not possible due to performance reasons, call {@link
   *           Workbench#clearOverlays()} after this method.
   */
  public final boolean hideOverlay(Region overlay) {
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
  public final void clearOverlays() {
    LOGGER.trace("clearOverlays");
    nonBlockingOverlaysShown.clear();
    blockingOverlaysShown.clear();
    overlays.clear();
  }

  /**
   * Shows a {@link WorkbenchDialog} in the view.
   *
   * @param dialog to be shown
   * @return  the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane},
   *           the result will be {@link ButtonType#CANCEL}.
   *           All dialogs are non-blocking by default. If you want to change this behavior, use
   *           {@link WorkbenchDialog#builder} to create a dialog and show it using
   *           {@link Workbench#showDialog(WorkbenchDialog)}.
   */
  public final WorkbenchDialog showDialog(WorkbenchDialog dialog) {
    DialogControl dialogControl = dialog.getDialogControl();
    dialogControl.setWorkbench(this);
    showOverlay(dialogControl, dialog.isBlocking());
    return dialog;
  }

  /**
   * Shows an error dialog in the view.
   *
   * @param title    of the dialog
   * @param message  of the dialog
   * @param onResult the action to perform when a button of the dialog was pressed, providing the
   *                 {@link ButtonType} that was pressed
   * @return the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane}, the
   *           result will be {@link ButtonType#CANCEL}.
   */
  public final WorkbenchDialog showErrorDialog(String title,
                                               String message,
                                               Consumer<ButtonType> onResult) {
    WorkbenchDialog dialog =
        WorkbenchDialog.builder(title, message, Type.ERROR).onResult(onResult).build();
    return showDialog(dialog);
  }

  /**
   * Shows an error dialog in the view with a stacktrace of the {@code exception}.
   *
   * @param title     of the dialog
   * @param message   of the dialog
   * @param exception of which the stacktrace should be shown
   * @param onResult  the action to perform when a button of the dialog was pressed, providing the
   *                  {@link ButtonType} that was pressed
   * @return the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane}, the
   *           result will be {@link ButtonType#CANCEL}.
   */
  public final WorkbenchDialog showErrorDialog(String title,
                                               String message,
                                               Exception exception,
                                               Consumer<ButtonType> onResult) {
    WorkbenchDialog dialog = WorkbenchDialog.builder(title, message, Type.ERROR)
        .exception(exception)
        .onResult(onResult)
        .build();
    return showDialog(dialog);
  }

  /**
   * Shows an error dialog in the view with {@code details} about the error.
   *
   * @param title    of the dialog
   * @param message  of the dialog
   * @param details  about the error
   * @param onResult the action to perform when a button of the dialog was pressed, providing the
   *                 {@link ButtonType} that was pressed
   * @return the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane}, the
   *           result will be {@link ButtonType#CANCEL}.
   */
  public final WorkbenchDialog showErrorDialog(String title,
                                               String message,
                                               String details,
                                               Consumer<ButtonType> onResult) {
    WorkbenchDialog dialog = WorkbenchDialog.builder(title, message, Type.ERROR)
        .details(details)
        .onResult(onResult)
        .build();
    return showDialog(dialog);
  }

  /**
   * Shows a warning dialog in the view.
   *
   * @param title    of the dialog
   * @param message  of the dialog
   * @param onResult the action to perform when a button of the dialog was pressed, providing the
   *                 {@link ButtonType} that was pressed
   * @return the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane},
   *           the result will be {@link ButtonType#CANCEL}.
   */
  public final WorkbenchDialog showWarningDialog(String title,
                                                 String message,
                                                 Consumer<ButtonType> onResult) {
    WorkbenchDialog dialog =
        WorkbenchDialog.builder(title, message, Type.WARNING).onResult(onResult).build();
    return showDialog(dialog);
  }

  /**
   * Shows a confirmation dialog in the view.
   *
   * @param title    of the dialog
   * @param message  of the dialog
   * @param onResult the action to perform when a button of the dialog was pressed, providing the
   *                 {@link ButtonType} that was pressed
   * @return the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane},
   *           the result will be {@link ButtonType#CANCEL}.
   */
  public final WorkbenchDialog showConfirmationDialog(String title,
                                                      String message,
                                                      Consumer<ButtonType> onResult) {
    WorkbenchDialog dialog =
        WorkbenchDialog.builder(title, message, Type.CONFIRMATION).onResult(onResult).build();
    return showDialog(dialog);
  }

  /**
   * Shows an information dialog in the view.
   *
   * @param title    of the dialog
   * @param message  of the dialog
   * @param onResult the action to perform when a button of the dialog was pressed, providing the
   *                 {@link ButtonType} that was pressed
   * @return the {@link WorkbenchDialog}, which will be shown
   * @implNote If the user closes a non-blocking dialog by clicking on the {@link GlassPane},
   *           the result will be {@link ButtonType#CANCEL}.
   */
  public final WorkbenchDialog showInformationDialog(String title,
                                                     String message,
                                                     Consumer<ButtonType> onResult) {
    WorkbenchDialog dialog =
        WorkbenchDialog.builder(title, message, Type.INFORMATION).onResult(onResult).build();
    return showDialog(dialog);
  }

  /**
   * Hides the {@code dialog} which was previously shown in the view
   * using {@link #showDialog(WorkbenchDialog)}.
   *
   * @param dialog to be hidden
   */
  public final void hideDialog(WorkbenchDialog dialog) {
    LOGGER.trace("hideDialog");
    DialogControl dialogControl = dialog.getDialogControl();
    dialogControl.setWorkbench(null);
    hideOverlay(dialogControl);
  }

  /**
   * Shows the {@code drawer} on the defined {@code side} in the {@link Workbench}.
   *
   * @param drawer to be shown
   * @param side   of the workbench, on which the {@code drawer} should be positioned
   * @implNote Sizes the drawer according to the computed size of {@code drawer}.
   *           However, it will take up a maximum of 90% of the screen, to allow the user to still
   *           close the drawer by clicking on the {@link GlassPane}.
   */
  public final void showDrawer(Region drawer, Side side) {
    showDrawer(drawer, side, -1);
  }

  /**
   * Shows the {@code drawer} on the defined {@code side} in the {@link Workbench}, ensuring the
   * {@code drawer} doesn't cover more than the specified {@code percentage}.
   *
   * @param drawer     to be shown
   * @param side       of the workbench, on which the {@code drawer} should be positioned
   * @param percentage value between 0 and 100, defining how much <b>maximum</b> coverage the drawer
   *                   should have or -1, to have the drawer size according to its computed size
   */
  public final void showDrawer(Region drawer, Side side, int percentage) {
    // fail fast
    if (!Range.closed(0, MAX_PERCENT).or(number -> number == -1).test(percentage)) {
      throw new IllegalArgumentException("Percentage needs to be between 0 and 100 or -1");
    }
    Pos position;
    drawer.minWidthProperty().unbind();
    drawer.maxWidthProperty().unbind();
    drawer.minHeightProperty().unbind();
    drawer.maxHeightProperty().unbind();
    switch (side) {
      case TOP:
        // fall through to BOTTOM
      case BOTTOM:
        position = side == Side.TOP ? Pos.TOP_LEFT : Pos.BOTTOM_LEFT;
        drawer.minWidthProperty().bind(widthProperty());
        if (percentage == -1) {
          bindDrawerHeight(drawer);
        } else {
          drawer.maxHeightProperty().bind(
              heightProperty().multiply((double) percentage / MAX_PERCENT));
        }
        break;
      case RIGHT:
        // fall through to LEFT
      default: // LEFT
        position = side == Side.LEFT ? Pos.TOP_LEFT : Pos.TOP_RIGHT;
        drawer.minHeightProperty().bind(heightProperty());
        if (percentage == -1) {
          bindDrawerWidth(drawer);
        } else {
          drawer.maxWidthProperty().bind(
              widthProperty().multiply((double) percentage / MAX_PERCENT));
        }
        break;
    }
    StackPane.setAlignment(drawer, position);
    drawer.getStyleClass().add("drawer");
    setDrawerSideShown(side);
    setDrawerShown(drawer);
  }

  private void bindDrawerWidth(Region drawer) {
    drawer.setMinWidth(0); // make sure minWidth isn't larger than maxWidth
    drawer.maxWidthProperty().bind(
        Bindings.createDoubleBinding(
            () -> {
              double computedWidth = drawer.prefWidth(-1);
              // calculate the width the drawer can take up without being so large that it can't be
              // hidden anymore by clicking on the GlassPane (GlassPane covers minimum of 10%)
              double maxDrawerWidth = widthProperty().get() * 0.9;
              return Math.min(computedWidth, maxDrawerWidth);
            }, drawer.maxWidthProperty(), widthProperty()
        )
    );
  }

  private void bindDrawerHeight(Region drawer) {
    drawer.setMinHeight(0); // make sure minHeight isn't larger than maxHeight
    drawer.maxHeightProperty().bind(
        Bindings.createDoubleBinding(
            () -> {
              double computedHeight = drawer.prefHeight(-1);
              // calculate the height the drawer can take up without being so large that it can't be
              // hidden anymore by clicking on the GlassPane (GlassPane covers minimum of 10%)
              double maxDrawerHeight = heightProperty().get() * 0.9;
              return Math.min(computedHeight, maxDrawerHeight);
            }, drawer.maxHeightProperty(), heightProperty()
        )
    );
  }

  /**
   * Hides the currently displayed drawer that was previously shown using
   * {@link #showDrawer(Region, Side)} or {@link #showDrawer(Region, Side, int)}.
   */
  public final void hideDrawer() {
    setDrawerShown(null);
    setDrawerSideShown(null);
  }

  public final void showNavigationDrawer() {
    showDrawer(navigationDrawer.get(), Side.LEFT);
  }

  public final void hideNavigationDrawer() {
    hideDrawer();
  }

  // Mutators and Accessors
  /**
   * Returns an unmodifiableObservableList of the currently open modules.
   * @return an unmodifiableObservableList of the currently open modules.
   */
  public final ObservableList<WorkbenchModule> getOpenModules() {
    return FXCollections.unmodifiableObservableList(openModules);
  }

  private ListProperty<WorkbenchModule> openModulesProperty() {
    return openModules;
  }

  /**
   * Returns a list of the currently loaded modules.
   *
   * @return the list of all loaded modules
   * @implNote Use this method to add or remove modules at runtime.
   */
  public final ObservableList<WorkbenchModule> getModules() {
    return modules.get();
  }

  private ListProperty<WorkbenchModule> modulesProperty() {
    return modules;
  }

  public final WorkbenchModule getActiveModule() {
    return activeModule.get();
  }

  public final ReadOnlyObjectProperty<WorkbenchModule> activeModuleProperty() {
    return activeModule;
  }

  public final Node getActiveModuleView() {
    return activeModuleView.get();
  }

  public final ReadOnlyObjectProperty<Node> activeModuleViewProperty() {
    return activeModuleView;
  }

  public final boolean isSingleModuleLayout(){ return modules.size() == 1; }

  /**
   * Returns a {@link CompletableFuture}, which upon completion will cause the module to be closed
   * and if there was an ongoing stage closing process, it will re-initiate that process.
   */
  private CompletableFuture<Boolean> getModuleCloseable(WorkbenchModule module) {
    return moduleCloseableMap.get(module);
  }

  /**
   * Returns a list of the currently loaded toolbar controls on the left.
   *
   * @return a list of the currently loaded toolbar controls on the left.
   * @implNote Use this method to add or remove toolbar controls on the left at runtime.
   */
  public final ObservableList<ToolbarItem> getToolbarControlsLeft() {
    return toolbarControlsLeft;
  }

  /**
   * Returns a list of the currently loaded toolbar controls on the right.
   *
   * @return a list of the currently loaded toolbar controls on the right.
   * @implNote Use this method to add or remove toolbar controls on the right at runtime.
   */
  public final ObservableList<ToolbarItem> getToolbarControlsRight() {
    return toolbarControlsRight;
  }

  /**
   * Returns a map of all overlays, which have previously been opened, with their corresponding
   * model object {@link WorkbenchOverlay}.
   * @return a map of all overlays, which have previously been opened, with their corresponding
   *         model object {@link WorkbenchOverlay}.
   */
  public final ObservableMap<Region, WorkbenchOverlay> getOverlays() {
    return FXCollections.unmodifiableObservableMap(overlays);
  }

  public final NavigationDrawer getNavigationDrawer() {
    return navigationDrawer.get();
  }

  public final void setNavigationDrawer(NavigationDrawer navigationDrawer) {
    this.navigationDrawer.set(navigationDrawer);
  }

  public final ObjectProperty<NavigationDrawer> navigationDrawerProperty() {
    return navigationDrawer;
  }

  public final ObservableList<MenuItem> getNavigationDrawerItems() {
    return navigationDrawerItems;
  }

  public final ObservableList<Region> getNonBlockingOverlaysShown() {
    return FXCollections.unmodifiableObservableList(nonBlockingOverlaysShown);
  }

  public final ObservableList<Region> getBlockingOverlaysShown() {
    return FXCollections.unmodifiableObservableList(blockingOverlaysShown);
  }

  public final int getModulesPerPage() {
    return modulesPerPage.get();
  }

  public final void setModulesPerPage(int modulesPerPage) {
    this.modulesPerPage.set(modulesPerPage);
  }

  public final IntegerProperty modulesPerPageProperty() {
    return modulesPerPage;
  }

  public final Callback<Workbench, Tab> getTabFactory() {
    return tabFactory.get();
  }

  public final void setTabFactory(Callback<Workbench, Tab> tabFactory) {
    this.tabFactory.set(tabFactory);
  }

  public final ObjectProperty<Callback<Workbench, Tab>> tabFactoryProperty() {
    return tabFactory;
  }

  public final Callback<Workbench, Tile> getTileFactory() {
    return tileFactory.get();
  }

  public final void setTileFactory(Callback<Workbench, Tile> tileFactory) {
    this.tileFactory.set(tileFactory);
  }

  public final ObjectProperty<Callback<Workbench, Tile>> tileFactoryProperty() {
    return tileFactory;
  }

  public final Callback<Workbench, Page> getPageFactory() {
    return pageFactory.get();
  }

  public final void setPageFactory(Callback<Workbench, Page> pageFactory) {
    this.pageFactory.set(pageFactory);
  }

  public final ObjectProperty<Callback<Workbench, Page>> pageFactoryProperty() {
    return pageFactory;
  }

  public final int getAmountOfPages() {
    return amountOfPages.get();
  }

  public final ReadOnlyIntegerProperty amountOfPagesProperty() {
    return amountOfPages;
  }

  public final Region getDrawerShown() {
    return drawerShown.get();
  }

  private void setDrawerShown(Region drawerShown) {
    this.drawerShown.set(drawerShown);
  }

  public final ReadOnlyObjectProperty<Region> drawerShownProperty() {
    return drawerShown;
  }

  public final Side getDrawerSideShown() {
    return drawerSideShown.get();
  }

  private void setDrawerSideShown(Side drawerSideShown) {
    this.drawerSideShown.set(drawerSideShown);
  }

  public final ReadOnlyObjectProperty<Side> drawerSideShownProperty() {
    return drawerSideShown;
  }

  @Override
  public final String getUserAgentStylesheet() {
    return Workbench.class.getResource("css/main.css").toExternalForm();
  }
}
