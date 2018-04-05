package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import javafx.scene.Node;

/**
 * Created by François Martin on 13.03.18.
 */
public abstract class AbstractModule implements Module {
  private final String name;
  private final Node icon;

  protected Node tile;
  protected Node tab;
  protected Node content;

  /**
   * Superconstructor to be called by the implementing class.
   */
  protected AbstractModule(String name, Node icon, Node main) {
    this.name = name;
    this.icon = icon;
    this.content = main;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTab() {
    if (tab == null) {
      tab = new TabControl(name, icon);
    }
    return tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTile() {
    if (tile == null) {
      tile = new TileControl(name, icon);
    }
    return tile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node init(WorkbenchFx workbench) {
    return content;
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
    this.content = null;
  }

}
