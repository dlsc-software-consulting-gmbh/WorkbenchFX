package com.dlsc.workbenchfx.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 * Represents the base for a module, to be displayed in WorkbenchFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public interface Modular {

    ObjectProperty<Node> mainNode = new SimpleObjectProperty<>();
    ObjectProperty<Node> buttonNode = new SimpleObjectProperty<>();
    ObjectProperty<Node> tabNode = new SimpleObjectProperty<>();

    void start();
    void stop();

}
