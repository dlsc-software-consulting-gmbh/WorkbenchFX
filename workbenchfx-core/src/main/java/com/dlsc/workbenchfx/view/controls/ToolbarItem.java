package com.dlsc.workbenchfx.view.controls;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
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

  private static final String TOOLBAR_BUTTON = "toolbar-button"; // no arrow to the right
  private static final String TOOLBAR_LABEL = "toolbar-label"; // no arrow, no click effect
  private static final String TOOLBAR_COMBO_BOX = "toolbar-menu-button"; // color on showing

  private final StringProperty text = new SimpleStringProperty(this, "text");
  private final ObjectProperty<Node> icon = new SimpleObjectProperty<>(this, "icon");
  private final ObjectProperty<EventHandler<? super MouseEvent>> onClick =
      new SimpleObjectProperty<>(this, "onClick");
  private final ListProperty<MenuItem> items = new SimpleListProperty<>(this, "items",
      FXCollections.observableArrayList());

  public ToolbarItem() {
    onClick.addListener((observable, oldClick, newClick) -> {
      setOnMouseClicked(newClick);
      updateStyleClasses();
    });
    items.addListener((InvalidationListener) observable -> updateStyleClasses());
  }

  public ToolbarItem(String text) {
    this();
    setText(text);
    getStyleClass().setAll(TOOLBAR_LABEL);
  }

  public ToolbarItem(Node icon) {
    this();
    setIcon(icon);
    getStyleClass().setAll(TOOLBAR_LABEL);
  }

  public ToolbarItem(Node icon, String text) {
    this(text);
    setIcon(icon);
  }

  public ToolbarItem(String text, EventHandler<? super MouseEvent> onClick) {
    this(text);
    setOnClick(onClick);
    getStyleClass().setAll(TOOLBAR_BUTTON);
  }

  public ToolbarItem(Node icon, EventHandler<? super MouseEvent> onClick) {
    this(icon);
    setOnClick(onClick);
    getStyleClass().setAll(TOOLBAR_BUTTON);
  }

  public ToolbarItem(Node icon, String text, EventHandler<? super MouseEvent> onClick) {
    this(icon, text);
    setOnClick(onClick);
    getStyleClass().setAll(TOOLBAR_BUTTON);
  }

  public ToolbarItem(String text, MenuItem... items) {
    this(text);
    setItems(FXCollections.observableArrayList(items));
    getStyleClass().setAll(TOOLBAR_COMBO_BOX);
  }

  public ToolbarItem(Node icon, MenuItem... items) {
    this(icon);
    setItems(FXCollections.observableArrayList(items));
    getStyleClass().setAll(TOOLBAR_COMBO_BOX);
  }

  public ToolbarItem(Node icon, String text, MenuItem... items) {
    this(icon, text);
    setItems(FXCollections.observableArrayList(items));
    getStyleClass().setAll(TOOLBAR_COMBO_BOX);
  }

  private void updateStyleClasses() {
    getStyleClass().setAll(TOOLBAR_LABEL);
    if (null != getOnClick()) {
      getStyleClass().setAll(TOOLBAR_BUTTON);
    }
    if (0 != items.size()) {
      getStyleClass().setAll(TOOLBAR_COMBO_BOX);
    }
  }

  public String getText() {
    return text.get();
  }

  public StringProperty textProperty() {
    return text;
  }

  public void setText(String text) {
    this.text.set(text);
  }

  public Node getIcon() {
    return icon.get();
  }

  public ObjectProperty<Node> iconProperty() {
    return icon;
  }

  public void setIcon(Node icon) {
    this.icon.set(icon);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new ToolbarItemSkin(this);
  }

  public EventHandler<? super MouseEvent> getOnClick() {
    return onClick.get();
  }

  public ObjectProperty<EventHandler<? super MouseEvent>> onClickProperty() {
    return onClick;
  }

  public void setOnClick(EventHandler<? super MouseEvent> onClick) {
    this.onClick.set(onClick);
  }

  public ObservableList<MenuItem> getItems() {
    return items.get();
  }

  public ListProperty<MenuItem> itemsProperty() {
    return items;
  }

  public void setItems(ObservableList<MenuItem> items) {
    this.items.set(items);
  }
}