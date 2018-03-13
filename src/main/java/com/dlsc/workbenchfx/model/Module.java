package com.dlsc.workbenchfx.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * Represents the base for a module, to be displayed in WorkbenchFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public interface Module {

    StringProperty name = new SimpleStringProperty();
    ObjectProperty<Image> icon = new SimpleObjectProperty<>();
    ObjectProperty<Node> node = new SimpleObjectProperty<>();

}
