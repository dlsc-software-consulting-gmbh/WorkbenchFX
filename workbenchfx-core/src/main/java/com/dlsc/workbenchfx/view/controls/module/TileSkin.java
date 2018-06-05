package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
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

  private Button button;

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
