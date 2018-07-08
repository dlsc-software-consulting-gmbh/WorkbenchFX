package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.module.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

public class DropdownTestModule extends WorkbenchModule {
  private int itemsCount = 1;

  private final Button invertBtn = new Button("Invert Style");
  private final Button addMenuBtn = new Button("Add new Dropdown");
  private final Button addItemBtn = new Button("Add MenuItem to Dropdown");
  private final Button addLotItemBtn = new Button("Add 100 MenuItems to Dropdown");
  private final Button removeMenuBtn = new Button("Remove new Dropdown");
  private final Button removeItemBtn = new Button("Remove MenuItem from Dropdown");
  private final Button removeLotItemBtn = new Button("Remove 100 MenuItems from Dropdown");
  private final Dropdown customDropdown = Dropdown.of(
      "New MenuButton",
      new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION)
  );
  private final List<MenuItem> itemsLst = new ArrayList<>();

  private final GridPane customPane = new GridPane();

  public DropdownTestModule() {
    super("Dropdown Test", FontAwesomeIcon.QUESTION);
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    customPane.add(addMenuBtn, 0, 0);
    customPane.add(addItemBtn, 0, 1);
    customPane.add(addLotItemBtn, 0, 2);
    customPane.add(invertBtn, 0, 3);

    customPane.add(removeMenuBtn, 1, 0);
    customPane.add(removeItemBtn, 1, 1);
    customPane.add(removeLotItemBtn, 1, 2);


    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    invertBtn.setOnAction(event -> customDropdown.invertStyle());

    addMenuBtn.setOnAction(event -> getWorkbench().getToolbarControlsRight().add(customDropdown));
    removeMenuBtn.setOnAction(event -> getWorkbench().getToolbarControlsRight().remove(customDropdown));

    addItemBtn.setOnAction(event -> addItems(1));
    addLotItemBtn.setOnAction(event -> addItems(100));

    removeItemBtn.setOnAction(event -> removeItems(1));
    removeLotItemBtn.setOnAction(event -> removeItems(100));
  }

  private void addItems(int items) {
    for (int i = 0; i < items; i++) {
      itemsLst.add(new CustomMenuItem(new Label("New Item " + itemsCount++)));
      customDropdown.getItems().add(itemsLst.get(itemsLst.size() - 1));
    }
  }

  private void removeItems(int items) {
    for (int i = 0; i < items; i++) {
      if (itemsCount > 1) {
        customDropdown.getItems().remove(itemsLst.remove(itemsCount - 2));
        itemsCount--;
      }
    }
  }

  @Override
  public Node activate() {
    return customPane;
  }
}
