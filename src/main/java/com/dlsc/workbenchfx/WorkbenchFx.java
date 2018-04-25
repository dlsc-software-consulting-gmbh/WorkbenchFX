package com.dlsc.workbenchfx;

import static impl.org.controlsfx.ReflectionUtils.addUserAgentStylesheet;

import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.*;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
    public final int modulesPerPage;
    public static final String STYLE_CLASS_ACTIVE_TAB = "active-tab";
    private final List<Dropdown> dropdowns;

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
     * The factories which are called when creating Tabs, Tiles and Pages of Tiles for the Views.
     * They require a module whose attributes are used to create the Nodes.
     */
    private ObjectProperty<BiFunction<WorkbenchFx, Module, Node>> tabFactory =
            new SimpleObjectProperty<>(this, "tabFactory");
    private ObjectProperty<BiFunction<WorkbenchFx, Module, Node>> tileFactory =
            new SimpleObjectProperty<>(this, "tileFactory");
    private ObjectProperty<BiFunction<WorkbenchFx, Integer, Node>> pageFactory =
            new SimpleObjectProperty<>(this, "pageFactory");

    /**
     * Creates the Workbench window.
     */
    public static WorkbenchFx of(List<Dropdown> dropdowns, Module... modules) {
        return WorkbenchFx.builder(dropdowns, modules).build();
    }

    public static WorkbenchFx of(Module... modules) {
        return WorkbenchFx.builder(modules).build();
    }

    // TODO: add javadoc comment
    public static WorkbenchFxBuilder builder(List<Dropdown> dropdowns, Module... modules) {
        return new WorkbenchFxBuilder(dropdowns, modules);
    }

    public static WorkbenchFxBuilder builder(Module... modules) {
        return new WorkbenchFxBuilder(new ArrayList<>(), modules);
    }

    public static Dropdown createDropdown(Node iconNode, String title, String subtitle, Node... contentNodes) {
        return Dropdown.of(iconNode, title, subtitle, contentNodes);
    }

    public static class WorkbenchFxBuilder {
        // Required parameters
        private final Module[] modules;
        private List<Dropdown> dropdowns;
        // Optional parameters - initialized to default values
        private int modulesPerPage = 9;
        private BiFunction<WorkbenchFx, Module, Node> tabFactory = (workbench, module) -> {
            TabControl tabControl = new TabControl(module);
            workbench.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
                LOGGER.trace("Tab Factory - Old Module: " + oldModule);
                LOGGER.trace("Tab Factory - New Module: " + oldModule);
                if (module == newModule) {
                    tabControl.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
                    LOGGER.trace("STYLE SET");
                }
                if (module == oldModule) {
                    // switch from this to other tab
                    tabControl.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
                }
            });
            tabControl.setOnClose(e -> workbench.closeModule(module));
            tabControl.setOnActive(e -> workbench.openModule(module));
            tabControl.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
            return tabControl;
        };
        private BiFunction<WorkbenchFx, Module, Node> tileFactory = (workbench, module) -> {
            TileControl tileControl = new TileControl(module);
            tileControl.setOnActive(e -> workbench.openModule(module));
            return tileControl;
        };
        private BiFunction<WorkbenchFx, Integer, Node> pageFactory = (workbench, pageIndex) -> {
            final int columnsPerRow = 3;

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("tilePage");

            int position = pageIndex * workbench.modulesPerPage;
            int count = 0;
            int column = 0;
            int row = 0;

            while (count < workbench.modulesPerPage && position < workbench.getModules().size()) {
                Module module = workbench.getModules().get(position);
                Node tile = workbench.getTile(module);
                gridPane.add(tile, column, row);

                position++;
                count++;
                column++;

                if (column == columnsPerRow) {
                    column = 0;
                    row++;
                }
            }
            return gridPane;
        };

        private WorkbenchFxBuilder(List<Dropdown> dropdowns, Module... modules) {
            this.dropdowns = dropdowns;
            this.modules = modules;
        }

        /**
         * Defines how many modules should be shown per page on the home screen.
         *
         * @param modulesPerPage amount of modules to be shown per page
         * @return builder for chaining
         */
        public WorkbenchFxBuilder modulesPerPage(int modulesPerPage) {
            this.modulesPerPage = modulesPerPage;
            return this;
        }

        /**
         * Defines how {@link Node} should be created to be used as the tab in the view.
         *
         * @param tabFactory to be used to create the {@link Node} for the tabs
         * @return builder for chaining
         * @implNote Use this to replace the control which is used for the tab with your own
         * implementation.
         */
        public WorkbenchFxBuilder tabFactory(BiFunction<WorkbenchFx, Module, Node> tabFactory) {
            this.tabFactory = tabFactory;
            return this;
        }

        /**
         * Defines how {@link Node} should be created to be used as the tile in the home screen.
         *
         * @param tileFactory to be used to create the {@link Node} for the tiles
         * @return builder for chaining
         * @implNote Use this to replace the control which is used for the tile with your own
         * implementation.
         */
        public WorkbenchFxBuilder tileFactory(BiFunction<WorkbenchFx, Module, Node> tileFactory) {
            this.tileFactory = tileFactory;
            return this;
        }

        /**
         * Defines how a page with tiles of {@link Module}s should be created.
         *
         * @param pageFactory to be used to create the page for the tiles
         * @return builder for chaining
         * @implNote Use this to replace the page which is used in the home screen to display tiles of
         * the modules with your own implementation.
         */
        public WorkbenchFxBuilder pageFactory(BiFunction<WorkbenchFx, Integer, Node> pageFactory) {
            this.pageFactory = pageFactory;
            return this;
        }

        /**
         * Builds and fully initializes a {@link WorkbenchFx} object.
         *
         * @return the {@link WorkbenchFx} object
         */
        public WorkbenchFx build() {
            return new WorkbenchFx(this);
        }
    }

    private WorkbenchFx(WorkbenchFxBuilder builder) {
        modulesPerPage = builder.modulesPerPage;
        dropdowns = builder.dropdowns;

        tabFactory.set(builder.tabFactory);
        tileFactory.set(builder.tileFactory);
        pageFactory.set(builder.pageFactory);
        initModules(builder.modules);
        initViews();
        getChildren().add(workbenchFxView);
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        addUserAgentStylesheet("./com/dlsc/workbenchfx/css/main.css");
    }

    private void initModules(Module... modules) {
        this.modules.addAll(modules);

        // handle changes of the active module
        activeModule.addListener(
                (observable, oldModule, newModule) -> {
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

    public List<Dropdown> getDropdowns() {
        return dropdowns;
    }
}
