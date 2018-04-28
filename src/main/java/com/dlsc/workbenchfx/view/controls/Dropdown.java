package com.dlsc.workbenchfx.view.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the Dropdown control wich is used in the ToolBar of WorkbenchFX.
 * It's functionality is like a {@link MenuButton}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Dropdown extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(Dropdown.class.getName());

  private final String text;
  private final Node icon;
  private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

  private Dropdown(String text, Node icon, MenuItem... menuItems) {
    this.text = text;
    this.icon = icon;
    this.menuItems.addAll(menuItems);

    getStyleClass().add("toolbar-menu-button");
  }

  public static Dropdown of(String text, Node icon, MenuItem... menuItems) {
    return new Dropdown(text, icon, menuItems);
  }

  public static Dropdown of(String text, MenuItem... menuItems) {
    return new Dropdown(text, null, menuItems);
  }

  public static Dropdown of(Node icon, MenuItem... menuItems) {
    return new Dropdown(null, icon, menuItems);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DropdownSkin(this);
  }

  public Node getIcon() {
    return icon;
  }

  public String getText() {
    return text;
  }

  public ObservableList<MenuItem> getItems() {
    return menuItems;
  }
}
