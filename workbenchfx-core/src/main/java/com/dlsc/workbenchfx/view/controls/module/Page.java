package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link Page}s with {@link WorkbenchModule}s in
 * the add module screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Page extends Control {
  private static final Logger LOGGER = LogManager.getLogger(Page.class.getName());
  private static final int INITIAL_PAGE_INDEX = -1;
  private final Workbench workbench;
  private final ObservableList<WorkbenchModule> modules;
  private final IntegerProperty pageIndex;
  private final ObservableList<Tile> tiles;
  private final IntegerProperty modulesPerPage;
  private InvalidationListener modulesChangedListener;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Page(Workbench workbench) {
    this.workbench = workbench;
    pageIndex = new SimpleIntegerProperty(this, "pageIndex", INITIAL_PAGE_INDEX);
    modulesPerPage = workbench.modulesPerPageProperty();
    modules = workbench.getModules();
    tiles = FXCollections.observableArrayList();
    setupChangeListeners();
    updateTiles();
    getStyleClass().add("page-control");
  }

  private void setupChangeListeners() {
    // update tiles list whenever modules or the pageIndex of this page have changed
    modulesChangedListener = observable -> updateTiles();
    modules.addListener(modulesChangedListener);
    pageIndex.addListener(modulesChangedListener);
    modulesPerPage.addListener(modulesChangedListener);
  }

  private void updateTiles() {
    if (getPageIndex() == INITIAL_PAGE_INDEX) {
      LOGGER.debug("Page has not been initialized yet - skipping updates of tiles");
      return;
    }
    // remove any preexisting tiles in the list
    LOGGER.debug(String.format("Tiles in page %s are being updated", getPageIndex()));
    tiles.clear();
    LOGGER.trace(String.format("Page Index: %s, Modules Per Page: %s", getPageIndex(),
        workbench.getModulesPerPage()));
    int position = getPageIndex() * workbench.getModulesPerPage();
    modules.stream()
        .skip(position) // skip all tiles from previous pages
        .limit(workbench.getModulesPerPage()) // only take as many tiles as there are per page
        .map(module -> {
          // create tile
          Tile tile = workbench.getTileFactory().call(workbench);
          tile.setModule(module);
          return tile;
        })
        .map(Tile.class::cast)
        .forEachOrdered(tiles::add);
  }

  public final int getPageIndex() {
    return pageIndex.get();
  }

  /**
   * Defines the {@code pageIndex} of this {@link Page}.
   *
   * @param pageIndex to be represented by this {@link Page}
   */
  public final void setPageIndex(int pageIndex) {
    this.pageIndex.set(pageIndex);
  }

  public final IntegerProperty pageIndexProperty() {
    return pageIndex;
  }

  public final ObservableList<Tile> getTiles() {
    return FXCollections.unmodifiableObservableList(tiles);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new PageSkin(this);
  }
}
