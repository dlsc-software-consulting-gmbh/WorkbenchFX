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
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the ToolbarItem which is used in the {@link ToolbarControl}s of WorkbenchFX.
 * Depending on the Parameters defined in the constructor, the {@link ToolbarItem} changes its
 * visual appearance to either a {@link javafx.scene.control.Label}, a {@link Button} or a
 * {@link MenuButton}.
 * Using {@link #setGraphic(Node)} offers additionally the possibility to add custom content.
 * But doing this might require additional custom styling.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarItem extends Control {

  private static final Logger LOGGER =
      LogManager.getLogger(ToolbarItem.class.getName());

  /**
   * Used to bind the dimensions of a ImageView to the ToolbarItems height
   * in order to reach a size of 16px, assuming a default height of 34px.
   */
  private static final double RESIZING_FACTOR = 0.47d;

  /**
   * The style class which is used to style the ToolbarItem as a Button.
   */
  private static final String TOOLBAR_BUTTON = "toolbar-button";

  /**
   * The style class which is used to style the ToolbarItem as a Label.
   */
  private static final String TOOLBAR_LABEL = "toolbar-label";

  /**
   * The style class which is used to style the ToolbarItem as a MenuButton.
   */
  private static final String TOOLBAR_COMBO_BOX = "toolbar-menu-button";

  private final StringProperty text = new SimpleStringProperty(this, "text");
  private final ObjectProperty<Node> graphic = new SimpleObjectProperty<>(this,
      "graphic");
  private final ObjectProperty<EventHandler<? super MouseEvent>> onClick =
      new SimpleObjectProperty<>(this, "onClick");
  private final ListProperty<MenuItem> items = new SimpleListProperty<>(this, "items",
      FXCollections.observableArrayList());

  /**
   * Creates a new empty {@link ToolbarItem}.
   * Use {@link #setText(String)}, {@link #setGraphic(Node)}, {@link #getItems()} or
   * {@link #setOnClick(EventHandler)} to define the content.
   */
  public ToolbarItem() {
    setupListeners();
    getStyleClass().setAll(TOOLBAR_LABEL);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.Label},
   * using the given {@code text} as label text.
   *
   * @param text the text to be displayed on the {@link ToolbarItem}
   */
  public ToolbarItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.Label},
   * using the given {@code graphic} as label graphic.
   *
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   */
  public ToolbarItem(Node graphic) {
    this();
    setGraphic(graphic);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.Label},
   * using the given {@code text} and {@code graphic} as label content.
   *
   * @param text    the text to be displayed on the {@link ToolbarItem}
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   */
  public ToolbarItem(String text, Node graphic) {
    this(text);
    setGraphic(graphic);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.Button},
   * using the given {@code text} as button text and the {@code onClick} as onMouseClicked event.
   *
   * @param text    the text to be displayed on the {@link ToolbarItem}
   * @param onClick the function to be called when a mouse button has been clicked
   *                (pressed and released) on this {@code Node}
   */
  public ToolbarItem(String text, EventHandler<? super MouseEvent> onClick) {
    this(text);
    setOnClick(onClick);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.Button},
   * using the given {@code graphic} as button graphic
   * and the {@code onClick} as onMouseClicked event.
   *
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param onClick the function to be called when a mouse button has been clicked
   *                (pressed and released) on this {@code Node}
   */
  public ToolbarItem(Node graphic, EventHandler<? super MouseEvent> onClick) {
    this(graphic);
    setOnClick(onClick);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.Button},
   * using the given {@code text} and {@code graphic} as button content
   * and the {@code onClick} as onMouseClicked event.
   *
   * @param text    the text to be displayed on the {@link ToolbarItem}
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param onClick the function to be called when a mouse button has been clicked
   *                (pressed and released) on this {@code Node}
   */
  public ToolbarItem(String text, Node graphic, EventHandler<? super MouseEvent> onClick) {
    this(text, graphic);
    setOnClick(onClick);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.MenuButton},
   * using the given {@code text} as button menu button text
   * and the {@code items} as its {@link MenuItem}s.
   *
   * @param text  the text to be displayed on the {@link ToolbarItem}
   * @param items the items to be displayed in the context-menu of the {@link ToolbarItem}
   */
  public ToolbarItem(String text, MenuItem... items) {
    this(text);
    setItems(FXCollections.observableArrayList(items));
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.MenuButton},
   * using the given {@code graphic} as button menu button graphic
   * and the {@code items} as its {@link MenuItem}s.
   *
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param items   the items to be displayed in the context-menu of the {@link ToolbarItem}
   */
  public ToolbarItem(Node graphic, MenuItem... items) {
    this(graphic);
    setItems(FXCollections.observableArrayList(items));
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link javafx.scene.control.MenuButton},
   * using the given {@code text} and {@code graphic} as button menu button content
   * and the {@code items} as its {@link MenuItem}s.
   *
   * @param text  the text to be displayed on the {@link ToolbarItem}
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param items   the items to be displayed in the context-menu of the {@link ToolbarItem}
   */
  public ToolbarItem(String text, Node graphic, MenuItem... items) {
    this(text, graphic);
    setItems(FXCollections.observableArrayList(items));
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

        // Binds the dimensions of a ImageView to the ToolbarItems height
        // in order to reach a size of 16px, assuming a default height of 34px.
        imageView.fitHeightProperty().bind(prefHeightProperty().multiply(RESIZING_FACTOR));
      }
    });
  }

  private void updateStyleClasses() {
    if (0 != items.size()) {
      getStyleClass().setAll(TOOLBAR_COMBO_BOX);
    } else if (null != getOnClick()) {
      getStyleClass().setAll(TOOLBAR_BUTTON);
    } else {
      getStyleClass().setAll(TOOLBAR_LABEL);
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