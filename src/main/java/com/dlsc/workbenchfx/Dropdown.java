package com.dlsc.workbenchfx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Dropdown extends HBox {
    private final Node iconView;
    private final String title;
    private final String subtitle;
    private final Node[] contentNodes;

    public Dropdown(Node iconView, String title, String subtitle, Node... contentNodes) {
        this.iconView = iconView;
        this.title = title;
        this.subtitle = subtitle;
        this.contentNodes = contentNodes;
        getChildren().addAll(
                iconView,
                new Label(title),
                new Label(subtitle)
        );
    }

    public Node getIconView() {
        return iconView;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Node[] getContentNodes() {
        return contentNodes;
    }
}
