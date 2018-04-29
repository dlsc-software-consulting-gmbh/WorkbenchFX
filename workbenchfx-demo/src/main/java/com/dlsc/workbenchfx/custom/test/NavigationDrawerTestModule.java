package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

public class NavigationDrawerTestModule extends AbstractModule {
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
    super("Navigation Drawer Test", FontAwesomeIcon.QUESTION);
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
    addSubmenuBtn.setOnAction(event -> addSubmenu(1,5));
    addLotSubmenuBtn.setOnAction(event -> addSubmenu(100,100));

    removeItemBtn.setOnAction(event -> removeItems(1));
    removeLotItemBtn.setOnAction(event -> removeItems(100));
    removeAllItemBtn.setOnAction(event -> removeAllItems());
  }

  private void removeAllItems() {
    ObservableList<MenuItem> navigationDrawerItems = workbench.getNavigationDrawerItems();
    MenuItem[] menuItems = new MenuItem[navigationDrawerItems.size()];
    for (int i = 0; i < navigationDrawerItems.size();  ++i) {
      menuItems[i] = navigationDrawerItems.get(i);
    }
    workbench.removeNavigationDrawerItems(menuItems);
  }

  private void addItems(int items) {
    for (int i = 0; i < items; i++) {
      MenuItem menuItem = new MenuItem("New Item " + itemsCount++);
      itemsLst.add(menuItem);
      workbench.addNavigationDrawerItems(menuItem);
    }
  }

  private void removeItems(int items) {
    for (int i = 0; i < items; i++) {
      if (itemsCount > 1) {
        workbench.removeNavigationDrawerItems(itemsLst.remove(itemsCount - 2));
        itemsCount--;
      }
    }
  }

  private void addSubmenu(int items, int subItems) {
    for (int i = 0; i < items; i++) {
      Menu subMenu = new Menu("New Submenu " + itemsCount++);
      itemsLst.add(subMenu);
      for(int j = 0; j < subItems; j++) {
        subMenu.getItems().add(new MenuItem("New Sub MenuItem " + (j+1)));
      }
      workbench.addNavigationDrawerItems(subMenu);
    }
  }



  @Override
  public Node activate() {
    return customPane;
  }
}
