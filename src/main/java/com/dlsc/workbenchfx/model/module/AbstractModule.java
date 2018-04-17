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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Skeletal implementation of a {@link Module}.
 * Extend this class to simply implement a new module and override methods as needed.
 */
public abstract class AbstractModule implements Module {

  protected WorkbenchFxModel workbenchModel;

  private final String name;
  private final Node tileIcon;
  private final Node tabIcon;

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
    this(name, new ImageView(icon), new ImageView(icon));
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    this(name, new FontAwesomeIconView(icon), new FontAwesomeIconView(icon));
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
    this.tileIcon = tileIcon;
    this.tabIcon = tabIcon;
  }

  private void setupRequests(TabControl tabControl) {
    tabControl.setOnCloseRequest(e -> workbenchModel.closeModule(this));
    tabControl.setOnActiveRequest(e -> workbenchModel.openModule(this));
  }

  private void setupRequests(TileControl tileControl) {
    tileControl.setOnActiveRequest(e -> workbenchModel.openModule(this));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(WorkbenchFxModel workbenchModel) {
    this.workbenchModel = workbenchModel;
    setTabFactory(integer -> {
      TabControl tabControl = new TabControl(name, tabIcon);
      setupRequests(tabControl);
      return tabControl;
    });
    setTileFactory(integer -> {
      TileControl tileControl = new TileControl(name, tileIcon);
      setupRequests(tileControl);
      return tileControl;
    });
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
