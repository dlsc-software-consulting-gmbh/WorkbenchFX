package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by François Martin on 13.03.18.
 */
public abstract class AbstractModule implements Module {

  // TODO: add title and icon
  // TODO: provide base implementation of tab and tile

  protected Node tile;
  protected Node tab;
  protected Node view;

  /**
   * Superconstructor to be called by the implementing class.
   * @param tile
   * @param tab
   * @param view
   */
  protected AbstractModule(Node tile, Node tab, Node view) {
    this.tile = tile;
    this.tab = tab;
    this.view = view;
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
    return view;
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
    this.view = null;
  }

}
