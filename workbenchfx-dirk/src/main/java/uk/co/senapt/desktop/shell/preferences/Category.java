package uk.co.senapt.desktop.shell.preferences;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

import javax.inject.Provider;

public class Category extends TreeItem<Category> {

    public Category() {
        setValue(this);
    }

    public Category(String name) {
        this();
        setName(name);
    }

    private final StringProperty id = new SimpleStringProperty(this, "id");

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    private final StringProperty name = new SimpleStringProperty(this, "name");

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    private final ObjectProperty<Provider<? extends Node>> contentProvider = new SimpleObjectProperty<>(this, "contentProvider");

    public Provider<? extends Node> getContentProvider() {
        return contentProvider.get();
    }

    public ObjectProperty<Provider<? extends Node>> contentProviderProperty() {
        return contentProvider;
    }

    public void setContentProvider(Provider<? extends Node> contentProvider) {
        this.contentProvider.set(contentProvider);
    }

    @Override
    public String toString() {
        return getName();
    }
}
