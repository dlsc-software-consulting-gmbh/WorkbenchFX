package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Skeletal implementation of a {@link Module}.
 * Extend this class to simply implement a new module and override methods as needed.
 */
public abstract class AbstractModule implements Module {

  protected WorkbenchFxModel workbenchModel;
  private String name;
  private Node tileIcon;
  private Node tabIcon;
  private FontAwesomeIcon faIcon;
  private Image imgIcon;

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, Image icon) {
    this.name = name;
    this.imgIcon = icon;
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    this.name = name;
    faIcon = icon;
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
    tileIcon = tile;
    tabIcon = tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(WorkbenchFxModel workbenchModel) {
    this.workbenchModel = workbenchModel;
  }

  @Override
  public Node getGraphic() {
    return Objects.isNull(faIcon) ? new ImageView(imgIcon) : new FontAwesomeIconView(faIcon);
  }

  @Override
  public String getName() {
    return Objects.isNull(name) ? "" : name;
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
