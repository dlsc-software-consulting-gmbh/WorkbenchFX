package com.dlsc.workbenchfx.model.module;

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

  private final Node tileIcon;
  private final Node tabIcon;

  protected Node tile;
  protected Node tab;

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, Image icon) {
    this.name = name;
    this.tileIcon = new ImageView(icon);
    this.tabIcon = new ImageView(icon);
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    this.name = name;
    this.tileIcon = new FontAwesomeIconView(icon);
    this.tabIcon = new FontAwesomeIconView(icon);
  }

  /**
   * Super constructor to be called by the implementing class.
   */
  protected AbstractModule(Node tileIcon, Node tabIcon) {
    this.tileIcon = tileIcon;
    this.tabIcon = tabIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTab() {
    if (tab == null) {
      tab = new TabControl(name, tabIcon);
    }
    return tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTile() {
    if (tile == null) {
      tile = new TileControl(name, tileIcon);
    }
    return tile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activate() {

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
  public void destroy() {
    // dereference objects to prevent memory leaks
    this.tile = null;
    this.tab = null;
  }

}
