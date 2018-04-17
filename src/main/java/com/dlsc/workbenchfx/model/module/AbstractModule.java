package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Skeletal implementation of a {@link Module}.
 * Extend this class to simply implement a new module and override methods as needed.
 */
public abstract class AbstractModule implements Module {
  private final String name;

  private final Node tile;
  private final Node tab;
  protected WorkbenchFxModel workbenchModel;

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, Image icon) {
    this.name = name;
    this.tile = new TileControl(name, new ImageView(icon));
    this.tab = new TabControl(name, new ImageView(icon));
    initTab();
  }

  private void initTab() {
    TabControl tabControl = (TabControl) tab;
    tabControl.setOnCloseRequest(e -> workbenchModel.closeModule(this));
    tabControl.setOnActiveRequest(e -> workbenchModel.openModule(this));
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    this.name = name;
    this.tile = new TileControl(name, new FontAwesomeIconView(icon));
    this.tab = new TabControl(name, new FontAwesomeIconView(icon));
    initTab();
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name     of this module
   * @param tileIcon node to be used for the icon of the tile in the home screen
   * @param tabIcon  node to be used for the icon of the tab
   * @throws IllegalArgumentException if tileIcon == tabIcon, since one {@link Node} instance
   *                                  can only be displayed once in a JavaFX scene graph.
   */
  protected AbstractModule(String name, Node tileIcon, Node tabIcon) {
    WorkbenchFxUtils.assertNodeNotSame(tileIcon, tabIcon);
    this.name = name;
    this.tile = new TileControl(name, tileIcon);
    this.tab = new TabControl(name, tabIcon);
    initTab();
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param tile node to be used for the tile control in the home screen
   * @param tab  node to be used for the tab control
   * @throws IllegalArgumentException if tile == tab, since one {@link Node} instance
   *                                  can only be displayed once in a JavaFX scene graph.
   */
  protected AbstractModule(Node tile, Node tab) {
    WorkbenchFxUtils.assertNodeNotSame(tile, tab);
    this.name = null;
    this.tile = tile;
    this.tab = tab;
    initTab();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(WorkbenchFxModel workbenchModel) {
    this.workbenchModel = workbenchModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTab() {
    return tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTile() {
    return tile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deactivate() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean destroy() {
    return true;
  }

}
