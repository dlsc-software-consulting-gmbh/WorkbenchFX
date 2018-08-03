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
 * Represents the ToolbarItem control which is used in the Toolbar of WorkbenchFX.
 * Its functionality is like that of a {@link MenuButton}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarItem extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(ToolbarItem.class.getName());

  private final String text;
  private final Node icon;
  private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

  private ToolbarItem(String text, Node icon, MenuItem... menuItems) {
    this.text = text;
    this.icon = icon;
    this.menuItems.addAll(menuItems);

    getStyleClass().add("toolbar-menu-button");
  }

  public static ToolbarItem of(String text, Node icon, MenuItem... menuItems) {
    return new ToolbarItem(text, icon, menuItems);
  }

  public static ToolbarItem of(String text, MenuItem... menuItems) {
    return new ToolbarItem(text, null, menuItems);
  }

  public static ToolbarItem of(Node icon, MenuItem... menuItems) {
    return new ToolbarItem(null, icon, menuItems);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new ToolbarItemSkin(this);
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
