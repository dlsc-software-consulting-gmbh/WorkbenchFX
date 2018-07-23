package uk.co.senapt.desktop.shell;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import uk.co.senapt.desktop.shell.model.PropertyDefinition;
import uk.co.senapt.desktop.shell.model.PropertySearchItem;
import uk.co.senapt.desktop.shell.skins.PropertySearchViewSkin;

public class PropertySearchView extends Control {

    public PropertySearchView () {
        getStyleClass().add("property-search-view");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertySearchViewSkin(this, items);
    }

    // AVAILABLE PROPERTIES
    private final ObservableList<PropertyDefinition> availableProperties = FXCollections.observableArrayList();

    public final ObservableList<PropertyDefinition> getAvailableProperties() {
        return availableProperties;
    }

    // ITEMS
    private final ObservableList<PropertySearchItem> items = FXCollections.observableArrayList();

    private final ObservableList<PropertySearchItem> itemsUnmodifiable = FXCollections.unmodifiableObservableList(items);

    /**
     * @return The list of items used as search criteria, this is unmodifiable.
     */
    public final ObservableList<PropertySearchItem> getItems() {
        return itemsUnmodifiable;
    }

    // LAYOUT TYPE
    private final ObjectProperty<LayoutType> layoutType = new SimpleObjectProperty<LayoutType>(this, "layoutType", LayoutType.LINEAR) {
        @Override
        public void set(LayoutType newValue) {
            super.set(Objects.requireNonNull(newValue));
        }
    };

    public final ObjectProperty<LayoutType> layoutTypeProperty () {
        return layoutType;
    }

    public final LayoutType getLayoutType () {
        return layoutTypeProperty().get();
    }

    public final void setLayoutType (LayoutType layoutType) {
        layoutTypeProperty().set(layoutType);
    }

    public enum LayoutType {
        GROUPED,
        LINEAR
    }
}
