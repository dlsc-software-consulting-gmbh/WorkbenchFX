package com.dlsc.workbenchfx.demo.controls;

import com.dlsc.workbenchfx.view.controls.MultilineLabel;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the skin of the corresponding {@link CustomTile}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CustomTileSkin extends SkinBase<CustomTile> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomTileSkin.class.getName());

  private VBox contentBox;
  private Label icon;
  private MultilineLabel textLbl;

  /**
   * Creates a new {@link CustomTileSkin} object for a corresponding {@link CustomTile}.
   *
   * @param tile the {@link CustomTile} for which this Skin is created
   */
  public CustomTileSkin(CustomTile tile) {
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
