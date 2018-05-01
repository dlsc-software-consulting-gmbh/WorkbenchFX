package com.dlsc.workbenchfx.view.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Represents the skin of the corresponding {@link NavigationDrawer}.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public class NavigationDrawerSkin extends SkinBase<NavigationDrawer> {

  private VBox menuContainer;

  /**
   * Creates the skin for the {@link NavigationDrawer} control.
   *
   * @param navigationDrawer to create this skin for
   */
  public NavigationDrawerSkin(NavigationDrawer navigationDrawer) {
    super(navigationDrawer);

    VBox drawerBox = new VBox();
    drawerBox.getStyleClass().add("drawer-box");

    BorderPane header = new BorderPane();
    header.getStyleClass().add("header");
    drawerBox.getChildren().add(header);

    menuContainer = new VBox();
    menuContainer.setFillWidth(true);
    menuContainer.getStyleClass().add("menu-container");

    PrettyScrollPane scrollPane = new PrettyScrollPane(menuContainer);
    drawerBox.getChildren().add(scrollPane);

    FontAwesomeIconView backIconView = new FontAwesomeIconView(FontAwesomeIcon.ARROW_LEFT);
    backIconView.setId("back-icon-view");
    Button backBtn = new Button("", backIconView);
    backBtn.setId("back-button");
    backBtn.setOnAction(evt -> navigationDrawer.getWorkbench().hideOverlay(navigationDrawer, true));
    BorderPane.setAlignment(backBtn, Pos.CENTER_LEFT);

    ImageView companyLogo = new ImageView();
    companyLogo.getStyleClass().add("logo");
    header.setTop(backBtn);
    header.setCenter(companyLogo);

    getChildren().add(drawerBox);

    navigationDrawer.getItems().addListener((Observable it) -> buildMenu());
    buildMenu();
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
