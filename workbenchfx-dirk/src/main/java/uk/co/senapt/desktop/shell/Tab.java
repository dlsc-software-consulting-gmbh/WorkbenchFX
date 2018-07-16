package uk.co.senapt.desktop.shell;

import javafx.beans.DefaultProperty;
import javafx.scene.Node;

@DefaultProperty("content")
public class Tab {

    private String icon;
    private String name;
    private Node graphic;
    private Node content;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getGraphic() {
        return graphic;
    }

    public void setGraphic(Node graphic) {
        this.graphic = graphic;
    }

    public Node getContent() {
        return content;
    }

    public void setContent(Node content) {
        this.content = content;
    }

}
