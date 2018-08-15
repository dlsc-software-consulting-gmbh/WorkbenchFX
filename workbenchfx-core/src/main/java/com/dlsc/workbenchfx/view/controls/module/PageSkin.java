package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.util.WorkbenchUtils;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link Page}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PageSkin extends SkinBase<Page> {
  private static final Logger LOGGER = LogManager.getLogger(PageSkin.class.getName());

  private final ObservableList<Tile> tiles;
  private GridPane tilePane;

  /**
   * Creates a new {@link PageSkin} object for a corresponding {@link Page}.
   *
   * @param page the {@link Page} for which this Skin is created
   */
  public PageSkin(Page page) {
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
