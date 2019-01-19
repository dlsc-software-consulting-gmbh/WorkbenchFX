package com.dlsc.workbenchfx.view.controls;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the skin of the corresponding {@link NavigationDrawer}.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public class NavigationDrawerSkin extends SkinBase<NavigationDrawer> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(NavigationDrawerSkin.class.getName());

  private VBox menuContainer;
  private NavigationDrawer navigationDrawer;
  private VBox drawerBox;
  private BorderPane header;
  private PrettyScrollPane scrollPane;
  private StackPane backIconShape;
  private Button backBtn;
  private Label companyLogo;

  /**
   * Creates the skin for the {@link NavigationDrawer} control.
   *
   * @param navigationDrawer to create this skin for
   */
  public NavigationDrawerSkin(NavigationDrawer navigationDrawer) {
    super(navigationDrawer);
    this.navigationDrawer = navigationDrawer;

    initializeParts();
    layoutParts();
    setupEventHandlers();
    setupValueChangedListeners();

    buildMenu();
  }

  /**
   * Initializes all parts of the skin.
   */
  private void initializeParts() {
    drawerBox = new VBox();
    drawerBox.getStyleClass().add("drawer-box");

    header = new BorderPane();
    header.getStyleClass().add("header");

    menuContainer = new VBox();
    menuContainer.getStyleClass().add("menu-container");

    scrollPane = new PrettyScrollPane(menuContainer);

    backIconShape = new StackPane();
    backIconShape.getStyleClass().add("shape");
    backBtn = new Button("", backIconShape);
    backBtn.getStyleClass().add("icon");
    backBtn.setId("back-button");

    companyLogo = new Label();
    companyLogo.getStyleClass().add("logo");
  }

  /**
   * Defines the layout of all parts in the skin.
   */
  private void layoutParts() {
    drawerBox.getChildren().addAll(header, scrollPane);

    menuContainer.setFillWidth(true);

    header.setTop(backBtn);
    header.setCenter(companyLogo);

    getChildren().add(drawerBox);
  }

  private void setupEventHandlers() {
    backBtn.setOnAction(evt -> navigationDrawer.hide());
  }

  private void setupValueChangedListeners() {
    navigationDrawer.getItems().addListener((Observable it) -> buildMenu());
  }

  private void buildMenu() {
    menuContainer.getChildren().clear();
    for (MenuItem item : getSkinnable().getItems()) {
      if (item instanceof Menu) {
        // item is a submenu
        menuContainer.getChildren().add(buildSubmenu(item));
      } else {
        // item is a regular menu item
        menuContainer.getChildren().add(buildMenuItem(item));
      }
    }
  }

  private MenuButton hoveredBtn;
  private boolean isTouchUsed = false;

  private MenuButton buildSubmenu(MenuItem item) {
    Menu menu = (Menu) item;
    MenuButton menuButton = new MenuButton();
    menuButton.setPopupSide(Side.RIGHT);
    menuButton.graphicProperty().bind(menu.graphicProperty());
    menuButton.textProperty().bind(menu.textProperty());
    menuButton.disableProperty().bind(menu.disableProperty());
    menuButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    menuButton.getStyleClass().addAll(item.getStyleClass());
    Bindings.bindContent(menuButton.getItems(), menu.getItems());

    // To determine if a TOUCH_RELEASED event happens.
    // The MOUSE_ENTERED results in an unexpected behaviour on touch events.
    // Event filter triggers before the handler.
    menuButton.addEventFilter(TouchEvent.TOUCH_RELEASED, e -> isTouchUsed = true);

    // Only when ALWAYS or SOMETIMES
    if (!Priority.NEVER.equals(getSkinnable().getMenuHoverBehavior())) {
      menuButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> { // Triggers on hovering over Menu
        if (isTouchUsed) {
          isTouchUsed = false;
          return;
        }
        // When ALWAYS, then trigger immediately. Else check if clicked before (case: SOMETIMES)
        if (Priority.ALWAYS.equals(getSkinnable().getMenuHoverBehavior())
            || (hoveredBtn != null && hoveredBtn.isShowing())) {
          menuButton.show(); // Shows the context-menu
          if (hoveredBtn != null && hoveredBtn != menuButton) {
            hoveredBtn.hide(); // Hides the previously hovered Button if not null and not self
          }
        }
        hoveredBtn = menuButton; // Add the button as previously hovered
      });
    }
    return menuButton;
  }

  private Button buildMenuItem(MenuItem item) {
    Button button = new Button();
    button.textProperty().bind(item.textProperty());
    button.graphicProperty().bind(item.graphicProperty());
    button.disableProperty().bind(item.disableProperty());
    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    button.getStyleClass().addAll(item.getStyleClass());
    button.setOnAction(item.getOnAction());

    // Only in cases ALWAYS and SOMETIMES: hide previously hovered button
    if (!Priority.NEVER.equals(getSkinnable().getMenuHoverBehavior())) {
      button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> { // Triggers on hovering over Button
        if (!isTouchUsed) {
          if (hoveredBtn != null) {
            hoveredBtn.hide(); // Hides the previously hovered Button if not null
          }
          hoveredBtn = null; // Sets it to null
        }
      });
    }
    return button;
  }
}
