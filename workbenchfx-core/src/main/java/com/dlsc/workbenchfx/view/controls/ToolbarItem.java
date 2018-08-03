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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the ToolbarItem control which is used in the Toolbar of WorkbenchFX. Its functionality
 * is like that of a {@link MenuButton}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarItem extends Control {

  private static final Logger LOGGER =
      LogManager.getLogger(ToolbarItem.class.getName());

  private static final double RESIZING_FACTOR = 0.47d;
  private static final String TOOLBAR_BUTTON = "toolbar-button"; // no arrow to the right
  private static final String TOOLBAR_LABEL = "toolbar-label"; // no arrow, no click effect
  private static final String TOOLBAR_COMBO_BOX = "toolbar-menu-button"; // color on showing

  private final StringProperty text = new SimpleStringProperty(this, "text");
  private final ObjectProperty<Node> graphic = new SimpleObjectProperty<>(this, "graphic");
  private final ObjectProperty<EventHandler<? super MouseEvent>> onClick =
      new SimpleObjectProperty<>(this, "onClick");
  private final ListProperty<MenuItem> items = new SimpleListProperty<>(this, "items",
      FXCollections.observableArrayList());

  public ToolbarItem() {
    setupListeners();
  }

  public ToolbarItem(String text) {
    this();
    setText(text);
    getStyleClass().setAll(TOOLBAR_LABEL);
  }

  public ToolbarItem(Node graphic) {
    this();
    setGraphic(graphic);
    getStyleClass().setAll(TOOLBAR_LABEL);
  }

  public ToolbarItem(String text, Node graphic) {
    this(text);
    setGraphic(graphic);
  }

  public ToolbarItem(String text, EventHandler<? super MouseEvent> onClick) {
    this(text);
    setOnClick(onClick);
    getStyleClass().setAll(TOOLBAR_BUTTON);
  }

  public ToolbarItem(Node graphic, EventHandler<? super MouseEvent> onClick) {
    this(graphic);
    setOnClick(onClick);
    getStyleClass().setAll(TOOLBAR_BUTTON);
  }

  public ToolbarItem(String text, Node graphic, EventHandler<? super MouseEvent> onClick) {
    this(text, graphic);
    setOnClick(onClick);
    getStyleClass().setAll(TOOLBAR_BUTTON);
  }

  public ToolbarItem(String text, MenuItem... items) {
    this(text);
    setItems(FXCollections.observableArrayList(items));
    getStyleClass().setAll(TOOLBAR_COMBO_BOX);
  }

  public ToolbarItem(Node graphic, MenuItem... items) {
    this(graphic);
    setItems(FXCollections.observableArrayList(items));
    getStyleClass().setAll(TOOLBAR_COMBO_BOX);
  }

  public ToolbarItem(String text, Node graphic, MenuItem... items) {
    this(text, graphic);
    setItems(FXCollections.observableArrayList(items));
    getStyleClass().setAll(TOOLBAR_COMBO_BOX);
  }

  private void setupListeners() {
    onClick.addListener((observable, oldClick, newClick) -> {
      setOnMouseClicked(newClick);
      updateStyleClasses();
    });
    items.addListener((InvalidationListener) observable -> updateStyleClasses());
    graphic.addListener((observable, oldIcon, newIcon) -> {
      if (newIcon instanceof ImageView) {
        ImageView imageView = ((ImageView) newIcon);
        imageView.setPreserveRatio(true);

        // Binds the dimensions of the ImageView to the dropdown's height.
        // Resizes the image with a RESIZING_FACTOR in order to fit in the ToolbarItem
        // and reach a size of 16px by a default height of 34px.
        imageView.fitHeightProperty().bind(prefHeightProperty().multiply(RESIZING_FACTOR));
      }
    });
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

  public Node getGraphic() {
    return graphic.get();
  }

  public ObjectProperty<Node> graphicProperty() {
    return graphic;
  }

  public void setGraphic(Node graphic) {
    this.graphic.set(graphic);
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