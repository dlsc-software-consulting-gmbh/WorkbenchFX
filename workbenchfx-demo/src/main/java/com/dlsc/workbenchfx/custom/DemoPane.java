package com.dlsc.workbenchfx.custom;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.custom.calendar.CalendarModule;
import com.dlsc.workbenchfx.custom.notes.NotesModule;
import com.dlsc.workbenchfx.custom.preferences.PreferencesModule;
import com.dlsc.workbenchfx.custom.test.NavigationDrawerTestModule;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import java.util.function.BiFunction;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class DemoPane extends StackPane {

  private static final Logger LOGGER = LogManager.getLogger(DemoPane.class.getName());
  public WorkbenchFx workbenchFx;

  BiFunction<WorkbenchFx, Module, Node> tabFactory = (workbench, module) -> {
    TabControl tabControl = new TabControl(module);
    workbench.activeModuleProperty().addListener((observable, oldValue, newValue) -> {
      LOGGER.trace("Tab Factory - Old Module: " + oldValue);
      LOGGER.trace("Tab Factory - New Module: " + oldValue);
      if (module == newValue) {
        tabControl.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
        LOGGER.error("STYLE SET");
      }
      if (module == oldValue) {
        // switch from this to other tab
        tabControl.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
      }
    });
    tabControl.setOnClose(e -> workbench.closeModule(module));
    tabControl.setOnActive(e -> workbench.openModule(module));
    tabControl.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
    System.out.println("This tab was proudly created by SteffiFX");
    return tabControl;
  };

  BiFunction<WorkbenchFx, Module, Node> tileFactory = (workbench, module) -> {
    TileControl tileControl = new TileControl(module);
    tileControl.setOnActive(e -> workbench.openModule(module));
    System.out.println("This tile was proudly created by SteffiFX");
    return tileControl;
  };

  Callback<WorkbenchFx, Node> globalMenuFactory = workbench -> {
    NavigationDrawer globalMenu = new NavigationDrawer(workbench);
    StackPane.setAlignment(globalMenu, Pos.TOP_LEFT);
    globalMenu.maxWidthProperty().bind(workbench.widthProperty().divide(2));
    return globalMenu;
  };

  BiFunction<WorkbenchFx, Integer, Node> pageFactory = (workbench, pageIndex) -> {
    final int COLUMNS_PER_ROW = 2;

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

      if (column == COLUMNS_PER_ROW) {
        column = 0;
        row++;
      }
    }
    return gridPane;
  };

  public DemoPane() {
    // Navigation Drawer
    Menu menu1 = new Menu("Customer");
    Menu menu2 = new Menu("Tariff Management");
    Menu menu3 = new Menu("Complaints");

    MenuItem item11 = new MenuItem("Item 1.1");
    MenuItem item12 = new MenuItem("Item 1.2");
    MenuItem item13 = new MenuItem("Item 1.3");
    MenuItem item14 = new MenuItem("Item 1.4");

    Menu item21 = new Menu("Item 2.1");
    MenuItem item22 = new MenuItem("Item 2.2");

    MenuItem item211 = new MenuItem("Item 2.1.1");
    MenuItem item212 = new MenuItem("Item 2.1.2");
    MenuItem item213 = new MenuItem("Item 2.1.3");
    MenuItem item214 = new MenuItem("Item 2.1.4");
    MenuItem item215 = new MenuItem("Item 2.1.5");

    MenuItem item31 = new MenuItem("Item 3.1");
    MenuItem item32 = new MenuItem("Item 3.2");
    MenuItem item33 = new MenuItem("Item 3.3");

    MenuItem itemA = new MenuItem("Complaints");
    MenuItem itemB = new MenuItem("Printing");
    MenuItem itemC = new MenuItem("Settings");

    item21.getItems().addAll(item211,item212, item213, item214, item215);

    menu1.getItems().addAll(item11, item12, item13, item14);
    menu2.getItems().addAll(item21, item22);
    menu3.getItems().addAll(item31, item32, item33);

    // WorkbenchFX
    workbenchFx = WorkbenchFx.builder(
        new CalendarModule(),
        new NotesModule(),
        new PreferencesModule(),
        new NavigationDrawerTestModule()
    ).modulesPerPage(2)
        .tabFactory(tabFactory)
        .tileFactory(tileFactory)
        .pageFactory(pageFactory)
        .globalMenuFactory(globalMenuFactory)
        .navigationDrawer(menu1, menu2, menu3, itemA, itemB, itemC)
        .build();
    getChildren().add(workbenchFx);
  }

}
