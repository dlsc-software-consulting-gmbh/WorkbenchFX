package com.dlsc.workbenchfx.demo.modules.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ToolbarItemTestModule extends WorkbenchModule {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ToolbarItemTestModule.class.getName());

  private int itemsCount = 1;

  private final Button addMenuBtn = new Button("Add new ToolbarItem");
  private final Button addItemBtn = new Button("Add MenuItem to ToolbarItem");
  private final Button addLotItemBtn = new Button("Add 100 MenuItems to ToolbarItem");
  private final Button removeMenuBtn = new Button("Remove new ToolbarItem");
  private final Button removeItemBtn = new Button("Remove MenuItem from ToolbarItem");
  private final Button removeLotItemBtn = new Button("Remove 100 MenuItems from ToolbarItem");
  private final ToolbarItem customToolbarItem = new ToolbarItem("New MenuButton", new FontIcon(FontAwesome.EXCLAMATION));
  private final List<MenuItem> itemsLst = new ArrayList<>();

  private final GridPane customPane = new GridPane();
  private final Button removeAllItmsBtn = new Button("REMOVE ALL ITEMS");

  public ToolbarItemTestModule() {
    super("ToolbarItem Test", MaterialDesign.MDI_HELP);
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    customPane.add(addMenuBtn, 0, 0);
    customPane.add(addItemBtn, 0, 1);
    customPane.add(addLotItemBtn, 0, 2);

    customPane.add(removeMenuBtn, 1, 0);
    customPane.add(removeItemBtn, 1, 1);
    customPane.add(removeLotItemBtn, 1, 2);

    customPane.add(removeAllItmsBtn, 2, 0);

    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    addMenuBtn
        .setOnAction(event -> getWorkbench().getToolbarControlsRight().add(customToolbarItem));
    removeMenuBtn.setOnAction(event -> getWorkbench().getToolbarControlsRight().remove(
        customToolbarItem));

    addItemBtn.setOnAction(event -> addItems(1));
    addLotItemBtn.setOnAction(event -> addItems(100));

    removeItemBtn.setOnAction(event -> removeItems(1));
    removeLotItemBtn.setOnAction(event -> removeItems(100));

    removeAllItmsBtn.setOnAction(event -> {
      getWorkbench().getToolbarControlsLeft().clear();
      getWorkbench().getToolbarControlsRight().clear();
    });
  }

  private void addItems(int items) {
    for (int i = 0; i < items; i++) {
      itemsLst.add(new CustomMenuItem(new Label("New Item " + itemsCount++)));
      customToolbarItem.getItems().add(itemsLst.get(itemsLst.size() - 1));
    }
  }

  private void removeItems(int items) {
    for (int i = 0; i < items; i++) {
      if (itemsCount > 1) {
        customToolbarItem.getItems().remove(itemsLst.remove(itemsCount - 2));
        itemsCount--;
      }
    }
  }

  @Override
  public Node activate() {
    return customPane;
  }
}
