package com.dlsc.workbenchfx.view.controls;

import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

/**
 * Created by lemmi on 23.08.17.
 */
public class PrettyMenuButton extends MenuButton {

    public PrettyMenuButton() {
        super();
        init();
    }

    public PrettyMenuButton(Node content) {
        super("");
        init();
    }

    private void init() {
        skinProperty().addListener(it -> {
            // first bind, then add new scrollbars, otherwise the new bars will be found
        });

        getStyleClass().add("pretty-menu-button");
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
    }
}
