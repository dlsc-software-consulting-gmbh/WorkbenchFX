package com.dlsc.workbenchfx.view.controls.module;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link Tile}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class TileSkin extends SkinBase<Tile> {

  private static final Logger LOGGER = LogManager.getLogger(TileSkin.class.getName());

  private VBox contentBox;
  private Label textLbl;
  private Node icon;

  /**
   * Creates a new {@link TileSkin} object for a corresponding {@link Tile}.
   *
   * @param tile the {@link Tile} for which this Skin is created
   */
  public TileSkin(Tile tile) {
    super(tile);

    initializeParts();
    layoutParts();
    setupBindings();
    setupValueChangedListeners();
  }

  private void initializeParts() {
    contentBox = new VBox();
    contentBox.getStyleClass().add("tile-control");
    textLbl = new Label(getSkinnable().getName());
    textLbl.getStyleClass().add("text-label");
    icon = getSkinnable().getIcon();
    icon.getStyleClass().add("icon");
  }

  private void layoutParts() {
    contentBox.getChildren().addAll(
        icon,
        textLbl
    );
    contentBox.setAlignment(Pos.CENTER);
    getChildren().add(contentBox);
  }

  private void setupBindings() {
    textLbl.textProperty().bind(getSkinnable().nameProperty());
  }

  private void setupValueChangedListeners() {
    getSkinnable().iconProperty().addListener(observable -> icon = getSkinnable().getIcon());
  }
}
