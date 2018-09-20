package com.dlsc.workbenchfx.demo.modules.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

public class NavigationDrawerTestModule extends WorkbenchModule {
  private int itemsCount = 1;

  private final Button addMenuBtn = new Button("Add 1 MenuItem");
  private final Button addLotItemBtn = new Button("Add 100 MenuItems");
  private final Button addSubmenuBtn = new Button("Add 1 Submenu");
  private final Button addLotSubmenuBtn = new Button("Add 100 Submenus");

  private final Button removeItemBtn = new Button("Remove 1 MenuItem");
  private final Button removeLotItemBtn = new Button("Remove 100 MenuItems");
  private final Button removeAllItemBtn = new Button("Remove all MenuItems");

  private final List<MenuItem> itemsLst = new ArrayList<>();

  private final GridPane customPane = new GridPane();

  public NavigationDrawerTestModule() {
    super("Navigation Drawer Test", MaterialDesignIcon.HELP);
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    customPane.add(addMenuBtn, 0, 0);
    customPane.add(addLotItemBtn, 0, 1);
    customPane.add(addSubmenuBtn, 0, 2);
    customPane.add(addLotSubmenuBtn, 0, 3);

    customPane.add(removeItemBtn, 1, 0);
    customPane.add(removeLotItemBtn, 1, 1);
    customPane.add(removeAllItemBtn, 1, 2);

    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    addMenuBtn.setOnAction(event -> addItems(1));
    addLotItemBtn.setOnAction(event -> addItems(100));
    addSubmenuBtn.setOnAction(event -> addSubmenu(1, 5));
    addLotSubmenuBtn.setOnAction(event -> addSubmenu(100, 100));

    removeItemBtn.setOnAction(event -> removeItems(1));
    removeLotItemBtn.setOnAction(event -> removeItems(100));
    removeAllItemBtn.setOnAction(event -> removeAllItems());
  }

  private void removeAllItems() {
    getWorkbench().getNavigationDrawerItems().clear();
  }

  private void addItems(int items) {
    for (int i = 0; i < items; i++) {
      MenuItem menuItem = new MenuItem("New Item " + itemsCount++);
      itemsLst.add(menuItem);
      getWorkbench().getNavigationDrawerItems().add(menuItem);
    }
  }

  private void removeItems(int items) {
    for (int i = 0; i < items; i++) {
      if (itemsCount > 1) {
        getWorkbench().getNavigationDrawerItems().remove(itemsLst.remove(itemsCount - 2));
        itemsCount--;
      }
    }
  }

  private void addSubmenu(int items, int subItems) {
    for (int i = 0; i < items; i++) {
      Menu subMenu = new Menu("New Submenu " + itemsCount++);
      itemsLst.add(subMenu);
      for (int j = 0; j < subItems; j++) {
        subMenu.getItems().add(new MenuItem("New Sub MenuItem " + (j + 1)));
      }
      getWorkbench().getNavigationDrawerItems().add(subMenu);
    }
  }


  @Override
  public Node activate() {
    return customPane;
  }
}
