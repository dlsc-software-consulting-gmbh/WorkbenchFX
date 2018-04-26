package com.dlsc.workbenchfx.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
  private final Node[] contentNodes;

  private final BooleanProperty expanded = new SimpleBooleanProperty();

  private Dropdown(Node iconView, String title, Node... contentNodes) {
    this.iconView = iconView;
    this.title = title;
    this.contentNodes = contentNodes;

    init();
    setupListeners();
    setupEventHandlers();
    setupBindings();
  }

  public static Dropdown of(Node iconView, String title, Node... contentNodes) {
    return new Dropdown(iconView, title, contentNodes);
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
    for (Node contentNode : contentNodes) {
      contentNode.getStyleClass().add("contentNode");
      CustomMenuItem customMenuItem = new CustomMenuItem(contentNode);
      contentNode.getStyleClass().add("customMenuItem");
      if (!(contentNode instanceof ButtonBase)) {
        customMenuItem.setHideOnClick(false);
      }
      getItems().add(customMenuItem);
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

  }
}
