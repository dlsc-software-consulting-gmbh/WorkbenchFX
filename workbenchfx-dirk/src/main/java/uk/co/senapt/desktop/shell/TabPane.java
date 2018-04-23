package uk.co.senapt.desktop.shell;

import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import uk.co.senapt.desktop.shell.skins.TabPaneSkin;

@DefaultProperty("tabs")
public class TabPane extends Control {

    public TabPane () {
        getStyleClass().add("tab-pane");
    }

    public void addIconTab (String icon, Node content) {
        addTab(icon, null, content);
    }

    public void addNameTab (String name, Node content) {
        addTab(null, name, content);
    }

    public void addTab (String icon, String name, Node content) {
        Tab tab = new Tab();
        tab.setIcon(icon);
        tab.setName(name);
        tab.setContent(content);
        tabs.add(tab);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TabPaneSkin(this);
    }

    private final ObservableList<Tab> tabs = FXCollections.observableArrayList();

    public ObservableList<Tab> getTabs() {
        return tabs;
    }

    private final BooleanProperty fillHeader = new SimpleBooleanProperty(this, "fillHeader");

    public final BooleanProperty fillHeaderProperty () {
        return fillHeader;
    }

    public final boolean isFillHeader () {
        return fillHeaderProperty().get();
    }

    public final void setFillHeader (boolean fillHeader) {
        fillHeaderProperty().set(fillHeader);
    }

}
