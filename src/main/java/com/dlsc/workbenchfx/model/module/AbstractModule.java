package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * Skeletal implementation of a {@link Module}.
 * Extend this class to simply implement a new module and override methods as needed.
 */
public abstract class AbstractModule implements Module {

  protected WorkbenchFxModel workbenchModel;
  private ObjectProperty<Callback<Integer, Node>> tabFactory =
      new SimpleObjectProperty<Callback<Integer, Node>>(this, "tabFactory");
  private ObjectProperty<Callback<Integer, Node>> tileFactory =
      new SimpleObjectProperty<Callback<Integer, Node>>(this, "tileFactory");

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, Image icon) {
    initTab(name, icon);
    initTile(name, icon);
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    initTab(name, icon);
    initTile(name, icon);
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
    initTab(name, tabIcon);
    initTile(name, tileIcon);
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
    initTab(null, tab);
    initTile(null, tile);
  }

  private void initTab(String name, Node icon) {
    setTabFactory(integer -> {
      TabControl tabControl = new TabControl(name, icon);
      return setupRequests(tabControl);
    });
  }

  private void initTab(String name, FontAwesomeIcon icon) {
    setTabFactory(integer -> {
      TabControl tabControl = new TabControl(name, new FontAwesomeIconView(icon));
      return setupRequests(tabControl);
    });
  }

  private void initTab(String name, Image icon) {
    setTabFactory(integer -> {
      TabControl tabControl = new TabControl(name, new ImageView(icon));
      return setupRequests(tabControl);
    });
  }

  private TabControl setupRequests(TabControl tabControl) {
    tabControl.setOnCloseRequest(e -> workbenchModel.closeModule(this));
    tabControl.setOnActiveRequest(e -> workbenchModel.openModule(this));
    return tabControl;
  }

  private void initTile(String name, Node icon) {
    setTileFactory(integer -> {
      TileControl tileControl = new TileControl(name, icon);
      return setupRequests(tileControl);
    });
  }

  private void initTile(String name, FontAwesomeIcon icon) {
    setTileFactory(integer -> {
      TileControl tileControl = new TileControl(name, new FontAwesomeIconView(icon));
      return setupRequests(tileControl);
    });
  }

  private void initTile(String name, Image icon) {
    setTileFactory(integer -> {
      TileControl tileControl = new TileControl(name, new ImageView(icon));
      return setupRequests(tileControl);
    });
  }

  private TileControl setupRequests(TileControl tileControl) {
    tileControl.setOnActiveRequest(e -> workbenchModel.openModule(this));
    return tileControl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(WorkbenchFxModel workbenchModel) {
    this.workbenchModel = workbenchModel;
  }

  /**
   * @param value
   */
  public final void setTabFactory(Callback<Integer, Node> value) {
    tabFactory.set(value);
  }

  /**
   * @param value
   */
  public final void setTileFactory(Callback<Integer, Node> value) {
    tileFactory.set(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTab() {
    return tabFactory.get().call(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTile() {
    return tileFactory.get().call(0);
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
