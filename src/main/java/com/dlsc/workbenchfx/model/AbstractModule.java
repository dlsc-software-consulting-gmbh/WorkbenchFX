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

  protected Node tile;
  protected Node tab;
  protected Node view;
/*  StringProperty name = new SimpleStringProperty();
  ObjectProperty<Image> icon = new SimpleObjectProperty<>();*/

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
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getTile() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node init(WorkbenchFx workbench) {
    return null;
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

  }
/*
  public AbstractModule(String name, Image icon, Node main) {
    this.name.setValue(name);
    this.icon.setValue(icon);
    mainNode.set(main);
    Button button = new Button(name);
    button.setGraphic(new ImageView(this.icon.get()));
    buttonNode.setValue(button);
    tabNode.setValue(button);
  }*/

}
