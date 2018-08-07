package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.view.controls.MultilineLabel;
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
  private Label icon;
  private MultilineLabel textLbl;

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
  }

  private void initializeParts() {
    icon = new Label();
    icon.getStyleClass().add("icon");
    contentBox = new VBox();
    contentBox.getStyleClass().add("tile-box");
    textLbl = new MultilineLabel(getSkinnable().getName());
    textLbl.getStyleClass().add("text-lbl");
  }

  private void layoutParts() {
    contentBox.getChildren().addAll(icon, textLbl);
    getChildren().add(contentBox);
  }

  private void setupBindings() {
    icon.graphicProperty().bind(getSkinnable().iconProperty());
    textLbl.textProperty().bind(getSkinnable().nameProperty());
  }

}
