package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;

/**
 * Created by François Martin on 13.03.18.
 */
public abstract class AbstractModule implements Module {

  // TODO: add title and icon
  // TODO: provide base implementation of tab and tile

  protected Node tile;
  protected Node tab;
  protected Node content;

  /**
   * Superconstructor to be called by the implementing class.
   * @param tile node to be shown in the home screen
   * @param tab node to be shown in the toolbar, representing this module
   * @param content main content node of this module
   */
  protected AbstractModule(Node tile, Node tab, Node content) {
    this.tile = tile;
    this.tab = tab;
    this.content = content;
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
