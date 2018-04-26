package com.dlsc.workbenchfx.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
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

  private final HBox buttonBox = new HBox();
  private final VBox menuBox = new VBox();
  private final VBox descriptionBox = new VBox();

  private final FontAwesomeIconView arrowIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);

  private final Node iconView;
  private final Label titleLbl;
  private final Label subtitleLbl;
  private final Node[] contentNodes;

  private final BooleanProperty expanded = new SimpleBooleanProperty();

  private Dropdown(Node iconView, String title, String subtitle, Node... contentNodes) {
    this.iconView = iconView;
    this.titleLbl = new Label(title);
    this.subtitleLbl = new Label(subtitle);
    this.contentNodes = contentNodes;

    setText(title);
    setGraphic(iconView);
    for (Node node : contentNodes) {
//      setContextMenu(new ContextMenu(new MenuItem("", node)));
      getItems().add(new MenuItem("", node));
    }
    hide();

    init();
    setupListeners();
    setupEventHandlers();
    setupBindings();
//    getContextMenu().setAutoHide(false);
  }

  public static Dropdown of(Node iconView, String title, String subtitle, Node... contentNodes) {
    return new Dropdown(iconView, title, subtitle, contentNodes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    getStyleClass().add("dropdown");
    buttonBox.getStyleClass().add("buttonBox");

    iconView.getStyleClass().add("iconView");
    titleLbl.getStyleClass().add("titleLbl");
    subtitleLbl.getStyleClass().add("subtitleLbl");
    descriptionBox.getStyleClass().add("descriptionBox");
    arrowIcon.getStyleClass().add("arrowIcon");

    menuBox.getStyleClass().add("menuBox");
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
    descriptionBox.getChildren().addAll(
        titleLbl,
        subtitleLbl
    );

    buttonBox.getChildren().addAll(
        iconView,
        descriptionBox
    );

    if (contentNodes.length != 0) {
      buttonBox.getChildren().add(arrowIcon);
    }

    for (Node contentNode : contentNodes) {
      contentNode.getStyleClass().add("contentNode");
      HBox contentBox = new HBox(contentNode);
      contentBox.getStyleClass().add("contentBox");
      menuBox.getChildren().add(contentBox);
    }

    getChildren().addAll(
        buttonBox,
        menuBox
    );

//        menuBox.relocate(buttonBox.getLayoutX() - 2, contentY - (1 - userOptionsVisibility.get()) * menuBox.getHeight());
    menuBox.relocate(100, 0);
//        menuBox.relocate(500, -50);
    menuBox.setVisible(false);
//        translateYProperty().bind(menuBox.heightProperty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

  private void setupEventHandlers() {
    buttonBox.setOnMouseClicked(event -> expanded.set(!expanded.get()));
  }

  private void setupBindings() {
    menuBox.visibleProperty().bind(expanded);
    menuBox.prefWidthProperty().bind(widthProperty());

    if (this.iconView instanceof ImageView) {
      ImageView imageView = ((ImageView) this.iconView);
      // Calculate ratio
      double ratio = imageView.getImage().getWidth() / imageView.getImage().getHeight();

      // Bind the dimensions of the ImageView to the dropdown's height
      imageView.fitHeightProperty().bind(buttonBox.prefHeightProperty().multiply(.7));
      imageView.fitWidthProperty().bind(buttonBox.prefHeightProperty().multiply(.7).multiply(ratio));
    }
  }

  private void setupListeners() {
    expanded.addListener((observable, oldValue, newValue) ->
        arrowIcon.setIcon(newValue ? FontAwesomeIcon.ANGLE_UP : FontAwesomeIcon.ANGLE_DOWN)
    );
  }
}
