package com.dlsc.workbenchfx.view.controls;

import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

  private static final double RESIZING_FACTOR = 0.4d;
  private final String standardStyle = "dropdown";

  private final MenuButton menuButton;
  private final Node icon;
  private final ObservableList<MenuItem> menuItems;

  /**
   * Creates a new {@link DropdownSkin} object for a corresponding {@link Dropdown}.
   *
   * @param dropdown the {@link Dropdown} for which this Skin is created
   */
  public DropdownSkin(Dropdown dropdown) {
    super(dropdown);
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

  private void setupBindings() {
    if (icon instanceof ImageView) {
      ImageView imageView = ((ImageView) icon);
      // Calculate ratio
      double ratio = getImage(imageView).getWidth() / getImage(imageView).getHeight();

      // Binds the dimensions of the ImageView to the dropdown's height.
      // Resizes the image with a RESIZING_FACTOR in order to fit in the Dropdown
      // and reach a size of 16px.
      imageView.fitHeightProperty().bind(
          menuButton.prefHeightProperty().multiply(RESIZING_FACTOR)
      );
      imageView.fitWidthProperty().bind(
          menuButton.prefHeightProperty().multiply(RESIZING_FACTOR).multiply(ratio)
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
  }
}
