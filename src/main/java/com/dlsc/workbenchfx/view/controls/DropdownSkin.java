package com.dlsc.workbenchfx.view.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.skin.MenuButtonSkin;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by lemmi on 22.08.17.
 */
public class DropdownSkin extends SkinBase<Dropdown> {
  private static final Logger LOGGER =
      LogManager.getLogger(DropdownSkin.class.getName());

  private final MenuButton menuButton;
  private final Node graphic;
  private final ObservableList<MenuItem> menuItems;
  private StackPane arrowButton;

  public DropdownSkin(Dropdown dropdown) {
    super(dropdown);

    menuButton = new MenuButton();
    menuButton.getStyleClass().add("dropdown");

    String text = dropdown.getText();
    if (!Objects.isNull(text)) {
      menuButton.setText(text);
    }

    graphic = dropdown.getGraphic();
    if (!Objects.isNull(graphic)) {
      menuButton.setGraphic(graphic);
      graphic.getStyleClass().add("graphic");
    }

    menuItems = dropdown.getItems();
    for (MenuItem menuItem : menuItems) {
      menuItem.getStyleClass().add("content-node");
      menuButton.getItems().add(menuItem);
    }

    getChildren().add(menuButton);

    replaceArrowIcon();
    setupBindings();
    setupListeners();
  }

  private void replaceArrowIcon() {
    Platform.runLater(() -> {
      arrowButton = ((StackPane) ((MenuButtonSkin) menuButton.getSkin()).getChildren().get(1));
      arrowButton.getChildren().clear();
      FontAwesomeIconView angleDown = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
      angleDown.getStyleClass().add("angle-down");
      arrowButton.getChildren().add(angleDown);
    });
  }

  private void setupBindings() {
    if (graphic instanceof ImageView) {
      ImageView imageView = ((ImageView) graphic);
      // Calculate ratio
      double ratio = imageView.getImage().getWidth() / imageView.getImage().getHeight();

      // Bind the dimensions of the ImageView to the dropdown's height
      imageView.fitHeightProperty().bind(
          menuButton.prefHeightProperty().multiply(.5)
      );
      imageView.fitWidthProperty().bind(
          menuButton.prefHeightProperty().multiply(.5).multiply(ratio)
      );
    }
  }

  private void setupListeners() {
    // Sets the pref-width of the dropdown's items
    menuButton.showingProperty().addListener((observable, oldValue, newValue) -> {
      if (!Objects.isNull(menuItems) && newValue) {
        menuItems.get(0).getParentPopup().setStyle("-fx-min-width: " + menuButton.getWidth());
      }
    });

    // When the List of the current menuItems is changed, the view is updated.
    menuItems.addListener((ListChangeListener<? super MenuItem>) c -> {
      while (c.next()) {
        if (c.wasRemoved()) {
          for (MenuItem menuItem : c.getRemoved()) {
            LOGGER.debug("MenuItem " + menuItem + " removed");
            menuButton.getItems().remove(c.getFrom());
          }
        }
        if (c.wasAdded()) {
          for (MenuItem menuItem : c.getAddedSubList()) {
            LOGGER.debug("MenuItem " + menuItem + " added");
            menuButton.getItems().add(menuItem);
          }
        }
      }
    });

  }
}