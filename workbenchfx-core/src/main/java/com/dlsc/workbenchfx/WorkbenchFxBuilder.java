package com.dlsc.workbenchfx;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import java.util.function.BiFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 01.05.18.
 */
public final class WorkbenchFxBuilder {
  private static final Logger LOGGER = LogManager.getLogger(WorkbenchFx.class.getName());

  // Required parameters
  final Module[] modules;

  // Optional parameters - initialized to default values
  int modulesPerPage = 9;

  BiFunction<WorkbenchFx, Module, Node> tabFactory = (workbench, module) -> {
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

  BiFunction<WorkbenchFx, Module, Node> tileFactory = (workbench, module) -> {
    TileControl tileControl = new TileControl(module);
    tileControl.setOnActive(e -> workbench.openModule(module));
    return tileControl;
  };

  BiFunction<WorkbenchFx, Integer, Node> pageFactory = (workbench, pageIndex) -> {
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

  ObservableList<Node> toolBarControls = FXCollections.observableArrayList();

  Callback<WorkbenchFx, Node> navigationDrawerFactory = workbench -> {
    // Defines the width of the navigationDrawer.
    // The value represents the percentage of the window which will be covered.
    final double widthPercentage = .333;

    NavigationDrawer navigationDrawer = new NavigationDrawer(workbench);
    StackPane.setAlignment(navigationDrawer, Pos.TOP_LEFT);
    navigationDrawer.maxWidthProperty().bind(workbench.widthProperty().multiply(widthPercentage));
    return navigationDrawer;
  };

  MenuItem[] navigationDrawerItems;

  Callback<WorkbenchFx, Node>[] overlays;

  WorkbenchFxBuilder(Module... modules) {
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
   * @implNote Use this to replace the page which is used in the home screen to display tiles of the
   * modules with your own implementation.
   */
  public WorkbenchFxBuilder pageFactory(BiFunction<WorkbenchFx, Integer, Node> pageFactory) {
    this.pageFactory = pageFactory;
    return this;
  }

  /**
   * Defines all of the overlays which should initially be loaded into the scene graph hidden, to be
   * later shown using {@link WorkbenchFx#showOverlay(Node, boolean)}.
   *
   * @param overlays callback to construct the overlays to be initially loaded into the scene graph
   *                 using a {@link WorkbenchFx} object
   * @return builder for chaining
   */
  public WorkbenchFxBuilder overlays(Callback<WorkbenchFx, Node>... overlays) {
    this.overlays = overlays;
    return this;
  }

  /**
   * Defines how the navigation drawer should be created.
   *
   * @param navigationDrawerFactory to be used to create the navigation drawer
   * @return builder for chaining
   * @implNote Use this to replace the navigation drawer, which is displayed when pressing the menu
   * icon, with your own implementation. To access the {@link MenuItem}s, use {@link
   * WorkbenchFx#getNavigationDrawerItems()}.
   */
  public WorkbenchFxBuilder navigationDrawerFactory(
      Callback<WorkbenchFx, Node> navigationDrawerFactory) {
    this.navigationDrawerFactory = navigationDrawerFactory;
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
  public WorkbenchFxBuilder navigationDrawer(MenuItem... navigationDrawerItems) {
    this.navigationDrawerItems = navigationDrawerItems;
    return this;
  }

  /**
   * Creates the Controls which are placed on top-right of the ToolBar.
   *
   * @param toolBarControls the {@code toolBarControls} which will be added to the ToolBar
   * @return the updated {@link WorkbenchFxBuilder}
   */
  public WorkbenchFxBuilder toolBarControls(Node... toolBarControls) {
    this.toolBarControls.addAll(toolBarControls);
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
