package com.dlsc.workbenchfx.demo.controls;

import com.dlsc.workbenchfx.util.WorkbenchUtils;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the skin of the corresponding {@link CustomPage}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CustomPageSkin extends SkinBase<CustomPage> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomPageSkin.class.getName());

  private final ObservableList<Tile> tiles;
  private GridPane tilePane;

  /**
   * Creates a new {@link CustomPageSkin} object for a corresponding {@link CustomPage}.
   *
   * @param page the {@link CustomPage} for which this Skin is created
   */
  public CustomPageSkin(CustomPage page) {
    super(page);
    tiles = page.getTiles();

    initializeParts();

    setupSkin(); // initial setup
    setupListeners(); // setup for changing modules

    getChildren().add(tilePane);
  }

  private void initializeParts() {
    tilePane = new GridPane();
    tilePane.getStyleClass().add("tile-pane");
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

    final int columnsPerRow = WorkbenchUtils.calculateColumnsPerRow(tiles.size());
    for (Tile tile : tiles) {
      tilePane.add(tile, column, row);
      column++;

      if (column == columnsPerRow) {
        column = 0;
        row++;
      }
    }
  }

}
