package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.view.controls.module.Tile;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link CustomPage}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CustomPageSkin extends SkinBase<CustomPage> {
  private static final Logger LOGGER = LogManager.getLogger(CustomPageSkin.class.getName());
  private static final int COLUMNS_PER_ROW = 3;

  private final ReadOnlyIntegerProperty pageIndex;
  private final ObservableList<Tile> tiles;

  private GridPane tilePane;

  /**
   * Creates a new {@link CustomPageSkin} object for a corresponding {@link CustomPage}.
   *
   * @param page the {@link CustomPage} for which this Skin is created
   */
  public CustomPageSkin(CustomPage page) {
    super(page);
    pageIndex = page.pageIndexProperty();
    tiles = page.getTiles();

    initializeParts();

    setupSkin(); // initial setup
    setupListeners(); // setup for changing modules

    getChildren().add(tilePane);
  }

  private void initializeParts() {
    tilePane = new GridPane();
    tilePane.getStyleClass().add("tile-page");
  }

  private void setupListeners() {
    LOGGER.trace("Add listener");
    tiles.addListener((InvalidationListener) observable -> setupSkin());
  }

  private void setupSkin() {
    // remove any pre-existing tiles
    tilePane.getChildren().clear();

    int column = 0;
    int row = 0;

    for (Tile tile : tiles) {
      tilePane.add(tile, column, row);
      column++;

      if (column == COLUMNS_PER_ROW) {
        column = 0;
        row++;
      }
    }
  }

}
