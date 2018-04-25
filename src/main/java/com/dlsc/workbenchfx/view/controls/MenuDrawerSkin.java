package com.dlsc.workbenchfx.view.controls;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Created by lemmi on 22.08.17.
 */
public class MenuDrawerSkin extends SkinBase<MenuDrawer> {

    private VBox menuContainer;

    public MenuDrawerSkin(MenuDrawer menuDrawer) {
        super(menuDrawer);

        VBox vBox = new VBox();

        BorderPane header = new BorderPane();
        header.getStyleClass().add("header");
        vBox.getChildren().add(header);

        menuContainer = new VBox();
        menuContainer.setFillWidth(true);
        menuContainer.getStyleClass().add("menu-container");

        PrettyScrollPane scrollPane = new PrettyScrollPane(menuContainer);
        vBox.getChildren().add(scrollPane);

        Label backIcon = new Label();
        backIcon.getStyleClass().add("shape");
        backIcon.setOnMouseClicked(evt -> menuDrawer.getWorkbench().setGlobalMenuShown(false));
        BorderPane.setAlignment(backIcon, Pos.CENTER_LEFT);

        ImageView companyLogo = new ImageView();
        companyLogo.getStyleClass().add("logo");
        companyLogo.setFitWidth(140);
        BorderPane.setMargin(companyLogo, new Insets(20, 0, 0, 0));
        BorderPane.setAlignment(companyLogo, Pos.CENTER_LEFT);
        header.setTop(backIcon);
        header.setCenter(companyLogo);

        getChildren().add(vBox);

        menuDrawer.getItems().addListener((Observable it) -> buildMenu());
        buildMenu();
    }

    private void buildMenu() {
        menuContainer.getChildren().clear();


        for (MenuItem item : getSkinnable().getItems()) {

            if (item instanceof Menu) {

                Menu menu = (Menu) item;
                MenuButton menuButton = new MenuButton();
                menuButton.setPopupSide(Side.RIGHT);
                menuButton.textProperty().bind(menu.textProperty());
                menuButton.disableProperty().bind(menu.disableProperty());
                menuButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                menuButton.getStyleClass().addAll(item.getStyleClass());
                Bindings.bindContent(menuButton.getItems(), menu.getItems());
                menuContainer.getChildren().add(menuButton);

            } else {

                Button button = new Button();
                button.textProperty().bind(item.textProperty());
                button.disableProperty().bind(item.disableProperty());
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.getStyleClass().addAll(item.getStyleClass());
                button.setOnAction(item.getOnAction());
                menuContainer.getChildren().add(button);

            }

        }
    }
}
