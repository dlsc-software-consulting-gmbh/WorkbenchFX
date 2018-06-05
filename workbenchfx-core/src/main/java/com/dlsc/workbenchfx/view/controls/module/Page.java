package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link Page}s with {@link Module}s in the home
 * screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Page extends Control {
  private static final Logger LOGGER = LogManager.getLogger(Page.class.getName());
  private final Workbench workbench;
  private final ObservableList<Module> modules;
  private final IntegerProperty pageIndex;
  private final ObservableList<Tile> tiles;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Page(Workbench workbench) {
    this.workbench = workbench;
    pageIndex = new SimpleIntegerProperty();
    modules = workbench.getModules();
    tiles = FXCollections.observableArrayList();
    setupChangeListeners();
    updateTiles();
  }

  private void setupChangeListeners() {
    // update tiles list whenever modules or the pageIndex of this page have changed
    InvalidationListener modulesChangedListener = observable -> updateTiles();
    modules.addListener(modulesChangedListener);
    pageIndex.addListener(modulesChangedListener);
  }

  // TODO: test this method
  private void updateTiles() {
    // remove any preexisting tiles in the list
    tiles.clear();
    int position = getPageIndex() * workbench.modulesPerPage;
    modules.stream()
        .skip(position) // skip all tiles from previous pages
        .limit(workbench.modulesPerPage) // only take as many tiles as there are per page
        .map(workbench::getTile)
        .map(Tile.class::cast)
        .forEachOrdered(tiles::add);
  }

  /**
   * Defines the {@code pageIndex} of this {@link Page}.
   *
   * @param pageIndex to be represented by this {@link Page}
   */
  public final void setPageIndex(int pageIndex) {
    this.pageIndex.set(pageIndex);
  }

  public int getPageIndex() {
    return pageIndex.get();
  }

  public ReadOnlyIntegerProperty pageIndexProperty() {
    return pageIndex;
  }

  public ObservableList<Tile> getTiles() {
    return FXCollections.unmodifiableObservableList(tiles);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new PageSkin(this);
  }
}
