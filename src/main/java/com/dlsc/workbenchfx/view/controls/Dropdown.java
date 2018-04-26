package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.view.View;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 29.12.17.
 */
public class Dropdown extends MenuButton implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(Dropdown.class.getName());

  private final Node iconView;
  private String title;
  private final MenuItem[] menuItems;

  private final BooleanProperty expanded = new SimpleBooleanProperty();

  private Dropdown(Node iconView, String title, MenuItem... menuItems) {
    this.iconView = iconView;
    this.title = title;
    this.menuItems = menuItems;

    init();
    setupListeners();
    setupEventHandlers();
    setupBindings();
  }

  public static Dropdown of(Node iconView, String title, MenuItem... menuItems) {
    return new Dropdown(iconView, title, menuItems);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    getStyleClass().add("dropdown");
    iconView.getStyleClass().add("iconView");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    setText(title);
    setGraphic(iconView);
    for (MenuItem menuItem : menuItems) {
      menuItem.getStyleClass().add("contentNode");
      menuItem.getStyleClass().add("customMenuItem");
      getItems().add(menuItem);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

  private void setupEventHandlers() {

  }

  private void setupBindings() {
    if (this.iconView instanceof ImageView) {
      ImageView imageView = ((ImageView) this.iconView);
      // Calculate ratio
      double ratio = imageView.getImage().getWidth() / imageView.getImage().getHeight();

      // Bind the dimensions of the ImageView to the dropdown's height
      imageView.fitHeightProperty().bind(prefHeightProperty().multiply(.5));
      imageView.fitWidthProperty().bind(prefHeightProperty().multiply(.5).multiply(ratio));
    }
  }

  private void setupListeners() {
    // Sets the pref-width of the dropdown's items
    showingProperty().addListener((observable, oldValue, newValue) -> {
      if (!Objects.isNull(getItems()) && newValue) {
        getItems().get(0).getParentPopup().setStyle("-fx-min-width: " + getWidth());
      }
    });
  }
}
