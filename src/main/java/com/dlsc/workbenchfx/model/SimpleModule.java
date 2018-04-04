package com.dlsc.workbenchfx.model;

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
public class SimpleModule implements Modular {

  StringProperty name = new SimpleStringProperty();
  ObjectProperty<Image> icon = new SimpleObjectProperty<>();

  public SimpleModule(String name, Image icon, Node main) {
    this.name.setValue(name);
    this.icon.setValue(icon);
    mainNode.set(main);
    Button button = new Button(name);
    button.setGraphic(new ImageView(this.icon.get()));
    buttonNode.setValue(button);
    tabNode.setValue(button);
  }

  public static SimpleModule of(String name, Image icon, Node main) {
    return new SimpleModule(name, icon, main);
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }
}
