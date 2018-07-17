package com.dlsc.workbenchfx.view.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link NavigationDrawer}.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public class NavigationDrawerSkin extends SkinBase<NavigationDrawer> {

  private static final Logger LOGGER = LogManager.getLogger(NavigationDrawerSkin.class.getName());

  private VBox menuContainer;
  private NavigationDrawer navigationDrawer;
  private VBox drawerBox;
  private BorderPane header;
  private PrettyScrollPane scrollPane;
  private Button backBtn;
  private ImageView companyLogo;
  private ReadOnlyDoubleProperty workbenchWidth;

  /**
   * Creates the skin for the {@link NavigationDrawer} control.
   *
   * @param navigationDrawer to create this skin for
   */
  public NavigationDrawerSkin(NavigationDrawer navigationDrawer) {
    super(navigationDrawer);
    this.navigationDrawer = navigationDrawer;

    initializeSelf();
    initializeParts();
    layoutParts();
    setupBindings();
    setupEventHandlers();
    setupValueChangedListeners();

    buildMenu();
  }

  private void setupBindings() {
    workbenchWidth = navigationDrawer.workbenchWidthProperty(); // strong reference to avoid GC
    navigationDrawer.maxWidthProperty().bind(workbenchWidth.multiply(.333));
  }

  /**
   * Initializes the skin.
   */
  private void initializeSelf() {
    navigationDrawer.getStyleClass().add("navigation-drawer");
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

    FontAwesomeIconView backIconView = new FontAwesomeIconView(FontAwesomeIcon.ARROW_LEFT);
    backIconView.setId("back-icon-view");
    backIconView.getStyleClass().add("icon-view");
    backBtn = new Button("", backIconView);
    backBtn.setId("back-button");

    companyLogo = new ImageView();
    companyLogo.getStyleClass().add("logo");
  }

  /**
   * Defines the layout of all parts in the skin.
   */
  private void layoutParts() {
    drawerBox.getChildren().addAll(header, scrollPane);

    menuContainer.setFillWidth(true);

    BorderPane.setAlignment(backBtn, Pos.CENTER_LEFT);

    header.setTop(backBtn);
    header.setCenter(companyLogo);

    getChildren().add(drawerBox);

    StackPane.setAlignment(navigationDrawer, Pos.TOP_LEFT);
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
    menuButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
//      double posX = getSkinnable().getScene().getWindow().getX();
      double posX = menuButton.getLayoutX();
      double posY = menuButton.getLayoutY();
//      double posY = getSkinnable().getScene().getWindow().getY();
      LOGGER.trace("PosX" + posX + " PosY" + posY);
      menuButton.fireEvent(
          new MouseEvent(MouseEvent.MOUSE_CLICKED, posX, posY, posX, posY, MouseButton.PRIMARY, 1,
              false, false, false, false, false, false, false, false, false, false, null));
//      menuButton.fire();
      LOGGER.trace("HOVER ON MENUITEM!!!");
    });
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
    return button;
  }
}
