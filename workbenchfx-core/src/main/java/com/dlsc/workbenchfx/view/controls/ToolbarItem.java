package com.dlsc.workbenchfx.view.controls;

import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the ToolbarItem which is used in the {@link ToolbarControl}s of WorkbenchFX.
 * Depending on the parameters defined in the constructor, the {@link ToolbarItem} changes its
 * visual appearance to either a {@link Label}, a {@link Button} or a {@link MenuButton}.
 * Using {@link #setGraphic(Node)} offers additionally the possibility to add custom content.
 * But doing this might require additional custom styling.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class ToolbarItem extends MenuButton {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ToolbarItem.class.getName());

  /**
   * According to the material design standards, a desktop icon should have a size of 16x16px.
   * @see <a href="https://material.io/design/iconography/system-icons.html#">material.io</a>
   */
  private static final double ICON_SIZE = 16d;

  /**
   * The icon should be placed in a square of 40x40px as clearance.
   */
  private static final double TOTAL_ICON_SIZE = 40d;

  /**
   * The WorkbenchFX styles define a toolbar height of 40px with a padding of 3px on top and bottom.
   */
  private static final double TOOLBAR_PADDING = 3d;

  /**
   * Used to bind the fit size dimensions of an {@link ImageView} to this Controls pref height.
   * The resizing factor ensures, that the {@link ImageView} doesn't exceed the defined icon size
   * of 16px.
   */
  private static final double ICON_RESIZING_FACTOR =
      ICON_SIZE / (TOTAL_ICON_SIZE - 2 * TOOLBAR_PADDING);

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
  private static final String TOOLBAR_MENU_BUTTON = "toolbar-menu-button";

  private final ObjectProperty<EventHandler<? super MouseEvent>> onClick =
      new SimpleObjectProperty<>(this, "onClick");

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
   * Creates a new {@link ToolbarItem} which appears like a {@link Label},
   * using the given {@code text} as label text.
   *
   * @param text to be displayed on the {@link ToolbarItem}
   */
  public ToolbarItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link Label},
   * using the given {@code graphic} as label graphic.
   *
   * @param graphic to be displayed on the {@link ToolbarItem}
   */
  public ToolbarItem(Node graphic) {
    this();
    setGraphic(graphic);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link Label},
   * using the given {@code text} and {@code graphic} as label content.
   *
   * @param text    to be displayed on the {@link ToolbarItem}
   * @param graphic to be displayed on the {@link ToolbarItem}
   */
  public ToolbarItem(String text, Node graphic) {
    this(text);
    setGraphic(graphic);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link Button},
   * using the given {@code text} as button text and the {@code onClick} as onMouseClicked event.
   *
   * @param text    to be displayed on the {@link ToolbarItem}
   * @param onClick the function to be called when a mouse button has been clicked
   *                (pressed and released) on this Control
   */
  public ToolbarItem(String text, EventHandler<? super MouseEvent> onClick) {
    this(text);
    setOnClick(onClick);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link Button},
   * using the given {@code graphic} as button graphic
   * and the {@code onClick} as onMouseClicked event.
   *
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param onClick the function to be called when a mouse button has been clicked
   *                (pressed and released) on this Control
   */
  public ToolbarItem(Node graphic, EventHandler<? super MouseEvent> onClick) {
    this(graphic);
    setOnClick(onClick);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link Button}, using the
   * given {@code text} and {@code graphic} as button content and the {@code onClick} as
   * onMouseClicked event.
   *
   * @param text    the text to be displayed on the {@link ToolbarItem}
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param onClick the function to be called when a mouse button has been clicked
   *                (pressed and released) on this Control
   */
  public ToolbarItem(String text, Node graphic, EventHandler<? super MouseEvent> onClick) {
    this(text, graphic);
    setOnClick(onClick);
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link MenuButton},
   * using the given {@code text} as menu button text
   * and the {@code items} as its {@link MenuItem}s.
   *
   * @param text  the text to be displayed on the {@link ToolbarItem}
   * @param items the items to be displayed in the context-menu of the {@link ToolbarItem}
   */
  public ToolbarItem(String text, MenuItem... items) {
    this(text);
    getItems().addAll((FXCollections.observableArrayList(items)));
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link MenuButton},
   * using the given {@code graphic} as button menu button graphic
   * and the {@code items} as its {@link MenuItem}s.
   *
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param items   the items to be displayed in the context-menu of the {@link ToolbarItem}
   */
  public ToolbarItem(Node graphic, MenuItem... items) {
    this(graphic);
    getItems().addAll((FXCollections.observableArrayList(items)));
  }

  /**
   * Creates a new {@link ToolbarItem} which appears like a {@link MenuButton},
   * using the given {@code text} and {@code graphic} as button menu button content
   * and the {@code items} as its {@link MenuItem}s.
   *
   * @param text  the text to be displayed on the {@link ToolbarItem}
   * @param graphic the graphic to be displayed on the {@link ToolbarItem}
   * @param items   the items to be displayed in the context-menu of the {@link ToolbarItem}
   */
  public ToolbarItem(String text, Node graphic, MenuItem... items) {
    this(text, graphic);
    getItems().addAll(FXCollections.observableArrayList(items));
  }

  private void setupListeners() {
    onClick.addListener((observable, oldClick, newClick) -> {
      setOnMouseClicked(newClick);
      updateStyleClasses();
    });
    getItems().addListener((InvalidationListener) observable -> updateStyleClasses());
    graphicProperty().addListener((observable, oldIcon, newIcon) -> {
      if (newIcon instanceof ImageView) {
        ImageView imageView = ((ImageView) newIcon);
        imageView.setPreserveRatio(true);

        // Binds the dimensions of the ImageView to the ToolbarItems height.
        imageView.fitHeightProperty().bind(prefHeightProperty().multiply(ICON_RESIZING_FACTOR));
      }
    });

    showingProperty().addListener((observable, wasShowing, isShowing) -> {
      if (!getItems().isEmpty() && isShowing) {
        getItems().get(0).getParentPopup().setStyle("-fx-min-width: " + getWidth());
      }
    });
  }

  private void updateStyleClasses() {
    if (!getItems().isEmpty()) {
      getStyleClass().setAll(TOOLBAR_MENU_BUTTON);
    } else if (!Objects.isNull(getOnClick())) {
      getStyleClass().setAll(TOOLBAR_BUTTON);
    } else {
      getStyleClass().setAll(TOOLBAR_LABEL);
    }
  }

  public final EventHandler<? super MouseEvent> getOnClick() {
    return onClick.get();
  }

  public final ObjectProperty<EventHandler<? super MouseEvent>> onClickProperty() {
    return onClick;
  }

  public final void setOnClick(EventHandler<? super MouseEvent> onClick) {
    this.onClick.set(onClick);
  }

}
