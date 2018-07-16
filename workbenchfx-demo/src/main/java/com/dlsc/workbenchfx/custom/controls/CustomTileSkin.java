package com.dlsc.workbenchfx.custom.controls;

import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link CustomTile}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CustomTileSkin extends SkinBase<CustomTile> {
  private static final Logger LOGGER = LogManager.getLogger(CustomTileSkin.class.getName());

  private Button button;

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
    setupEventHandlers();
    setupValueChangedListeners();
  }

  private void initializeParts() {
    button = new Button();
    button.getStyleClass().add("tile-control");
  }

  private void layoutParts() {
    getChildren().add(button);
  }

  private void setupBindings() {
    button.textProperty().bind(getSkinnable().nameProperty());
    button.graphicProperty().bind(getSkinnable().iconProperty());
  }

  private void setupEventHandlers(){
    button.setOnAction(e -> getSkinnable().open());
  }

  private void setupValueChangedListeners() {

  }
}
