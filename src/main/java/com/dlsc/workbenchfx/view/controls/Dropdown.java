package com.dlsc.workbenchfx.view.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by lemmi on 22.08.17.
 */
public class Dropdown extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(Dropdown.class.getName());

  private final String text;
  private final Node graphic;
  private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

  private Dropdown(String text, Node graphic, MenuItem... menuItems) {
    this.text = text;
    this.graphic = graphic;
    this.menuItems.addAll(menuItems);

    getStyleClass().add("toolbar-menu-button");
  }

  public static Dropdown of(String text, Node graphic, MenuItem... menuItems) {
    return new Dropdown(text, graphic, menuItems);
  }

  public static Dropdown of(String text, MenuItem... menuItems) {
    return new Dropdown(text, null, menuItems);
  }

  public static Dropdown of(Node graphic, MenuItem... menuItems) {
    return new Dropdown(null, graphic, menuItems);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DropdownSkin(this);
  }

  public Node getGraphic() {
    return graphic;
  }

  public String getText() {
    return text;
  }

  public ObservableList<MenuItem> getItems() {
    return menuItems;
  }
}
