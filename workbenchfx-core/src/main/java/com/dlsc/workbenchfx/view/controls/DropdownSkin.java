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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the Skin which is made for the {@link Dropdown}.
 * It uses a {@link MenuButton} to inherit the main functionality.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DropdownSkin extends SkinBase<Dropdown> {
  private static final Logger LOGGER =
      LogManager.getLogger(DropdownSkin.class.getName());

  private final Dropdown dropdown;

  private static final int ARROW_NODE_INDEX = 1;
  private static final double HALF_DROPDOWN_SIZE = 0.5d;
  private final String standardStyle = "dropdown";
  private final String invertedStyle = "dropdown-inverted";

  private final MenuButton menuButton;
  private final Node icon;
  private final ObservableList<MenuItem> menuItems;
  private StackPane arrowButtonPane;

  /**
   * Creates a new {@link DropdownSkin} object for a corresponding {@link Dropdown}.
   *
   * @param dropdown the {@link Dropdown} for which this Skin is created
   */
  public DropdownSkin(Dropdown dropdown) {
    super(dropdown);
    this.dropdown = dropdown;

    menuButton = new MenuButton();
    menuButton.getStyleClass().add(standardStyle);

    String text = dropdown.getText();
    if (!Objects.isNull(text)) {
      menuButton.setText(text);
    }

    icon = dropdown.getIcon();
    if (!Objects.isNull(icon)) {
      menuButton.setGraphic(icon);
      icon.getStyleClass().add("icon");
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

  /**
   * Retrieves the {@link Image} of a given {@link ImageView} and returns it.
   *
   * @param imageView the {@link ImageView} from which the {@link Image} should be extracted
   * @return the found {@link Image}
   */
  private static Image getImage(ImageView imageView) {
    return imageView.getImage();
  }

  /**
   * Replaces the default arrow icon of the {@code menuButton} with a custom fontawesome icon.
   */
  private void replaceArrowIcon() {
    Platform.runLater(() -> {
      arrowButtonPane =
          (StackPane) ((MenuButtonSkin) menuButton.getSkin()).getChildren().get(
              ARROW_NODE_INDEX);
      // Removes the old arrow
      arrowButtonPane.getChildren().clear();
      FontAwesomeIconView angleDown = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
      angleDown.getStyleClass().add("angle-down");
      arrowButtonPane.getChildren().add(angleDown);
    });
  }

  private void setupBindings() {
    if (icon instanceof ImageView) {
      ImageView imageView = ((ImageView) icon);
      // Calculate ratio
      double ratio = getImage(imageView).getWidth() / getImage(imageView).getHeight();

      // Binds the dimensions of the ImageView to the dropdown's height.
      // Resizes the image with a HALF_DROPDOWN_SIZE in order to fit in the Dropdown.
      imageView.fitHeightProperty().bind(
          menuButton.prefHeightProperty().multiply(HALF_DROPDOWN_SIZE)
      );
      imageView.fitWidthProperty().bind(
          menuButton.prefHeightProperty().multiply(HALF_DROPDOWN_SIZE).multiply(ratio)
      );
    }
  }

  private void setupListeners() {
    // Sets the pref-width of the dropdown's items when the ContextMenu is showing
    menuButton.showingProperty().addListener((observable, wasShowing, isShowing) -> {
      if (!menuItems.isEmpty() && isShowing) {
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
            menuButton.getItems().addAll(menuItem);
          }
        }
      }
    });

    // Changes orientation of the icon when the menu-items are showing
    menuButton.showingProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        ((FontAwesomeIconView) arrowButtonPane.getChildren().get(0))
            .setIcon(FontAwesomeIcon.ANGLE_UP);
      } else {
        ((FontAwesomeIconView) arrowButtonPane.getChildren().get(0))
            .setIcon(FontAwesomeIcon.ANGLE_DOWN);
      }
    });

    // Changes the styleClass either from dropdown to dropdown-inverted.
    // In the css, the style is applied.
    dropdown.invertedProperty().addListener((observable, oldIsInverted, newIsInverted) -> {
      if (newIsInverted) {
        menuButton.getStyleClass().remove(standardStyle);
        menuButton.getStyleClass().add(invertedStyle);
      } else {
        menuButton.getStyleClass().remove(invertedStyle);
        menuButton.getStyleClass().add(standardStyle);
      }
    });
  }
}
