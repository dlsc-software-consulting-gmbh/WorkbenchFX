package uk.co.senapt.desktop.shell.skins;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.co.senapt.desktop.shell.PropertySearchItemView;
import uk.co.senapt.desktop.shell.PropertySearchView;
import uk.co.senapt.desktop.shell.TabPane;
import uk.co.senapt.desktop.shell.model.PropertyDefinition;
import uk.co.senapt.desktop.shell.model.PropertySearchItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PropertySearchViewSkin extends SkinBase<PropertySearchView> {

    private final ObservableList<PropertySearchItem> items;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public PropertySearchViewSkin(PropertySearchView control, ObservableList<PropertySearchItem> items) {
        super(control);
        this.items = items;
        control.getAvailableProperties().addListener((Observable obs) -> build());
        control.layoutTypeProperty().addListener(obs -> build());
        build();
    }

    private void build () {
        items.clear();
        if (getSkinnable().getLayoutType() == PropertySearchView.LayoutType.LINEAR) {
            buildLinear();
        }
        else if (getSkinnable().getLayoutType() == PropertySearchView.LayoutType.GROUPED) {
            buildGrouped();
        }
        else {
            throw new UnsupportedOperationException("Layout Type not supported: " + getSkinnable().getLayoutType());
        }
    }

    private void buildLinear () {
        getChildren().setAll(new PropertySearchGroup(getSkinnable().getAvailableProperties()));
    }

    private void buildGrouped () {
        Map<String, List<PropertyDefinition>> propertiesByCategory = new LinkedHashMap<>();
        for (PropertyDefinition definition : getSkinnable().getAvailableProperties()) {
            List<PropertyDefinition> temp = propertiesByCategory.computeIfAbsent(definition.getCategory(), key -> new ArrayList<>());
            temp.add(definition);
        }

        if (propertiesByCategory.keySet().size() == 1) {
            buildLinear();
            return;
        }

        TabPane tabPane = new TabPane();
        tabPane.setFillHeader(true);
        for (Map.Entry<String, List<PropertyDefinition>> entry : propertiesByCategory.entrySet()) {
            tabPane.addNameTab(entry.getKey(), new PropertySearchGroup(entry.getValue()));
        }
        getChildren().setAll(tabPane);
    }

    private class PropertySearchGroup extends VBox {

        private final List<PropertyDefinition> properties;
        private final List<PropertyDefinition> usedProperties;
        private final EmptySearchRow emptyRow = new EmptySearchRow(this);

        PropertySearchGroup (List<PropertyDefinition> properties) {
            this.properties = properties;
            this.usedProperties = new ArrayList<>();
            getChildren().add(emptyRow);
            getStyleClass().add("property-search-group");
        }

        public List<PropertyDefinition> getAvailableProperties(PropertyDefinition value) {
            List<PropertyDefinition> availableProperties = new ArrayList<>(properties);
            List<PropertyDefinition> tempUsed = new ArrayList<>(usedProperties);
            tempUsed.remove(value);
            availableProperties.removeAll(tempUsed);
            return availableProperties;
        }

        public void removeRow(PropertySearchRow row) {
            PropertySearchItem item = row.getItem();
            updateUsedProperties(item.getProperty(), null, row);
            getChildren().remove(row);
            items.remove(item);
            updateAddRowButton();
        }

        public void addRow() {
            PropertySearchItem item = new PropertySearchItem();
            getChildren().add(getChildren().size() - 1, new PropertySearchRow(item, this));
            items.add(item);
            updateAddRowButton();
        }

        public void updateUsedProperties (PropertyDefinition oldValue, PropertyDefinition newValue, PropertySearchRow row) {
            if (newValue != null) {
                usedProperties.add(newValue);
            }
            usedProperties.remove(oldValue);
            updateAddRowButton();
        }

        public void updateAddRowButton () {
            emptyRow.setIconDisabled((getChildren().size() - 1) == properties.size() || getAvailableProperties(null).isEmpty());
        }
    }

    private static class PropertySearchRow extends HBox {

        private final PropertySearchItem item;
        private final PropertySearchItemView itemView;

        PropertySearchRow (PropertySearchItem item, PropertySearchGroup group) {
            this.item = item;

            Label icon = new Label();
            icon.getStyleClass().addAll("property-minus-icon", "property-search-icon");
            icon.setOnMouseClicked(evt -> group.removeRow(this));

            itemView = new PropertySearchItemView();
            itemView.getAvailableProperties().setAll(group.getAvailableProperties(null));
            itemView.propertyProperty().addListener((obs, oldValue, newValue) -> group.updateUsedProperties(oldValue, newValue, this));
            itemView.setItem(item);

            getChildren().addAll(icon, itemView);
            getStyleClass().add("property-search-row");
            HBox.setHgrow(itemView, Priority.ALWAYS);
        }

        public PropertySearchItem getItem() {
            return item;
        }

        public PropertySearchItemView getItemView() {
            return itemView;
        }
    }

    private static class EmptySearchRow extends HBox {
        private Label icon;
        EmptySearchRow (PropertySearchGroup group) {
            icon = new Label();
            icon.getStyleClass().addAll("property-plus-icon", "property-search-icon");
            icon.setOnMouseClicked(evt -> group.addRow());
            PropertySearchItemView itemView = new PropertySearchItemView();
            getChildren().addAll(icon, itemView);
            getStyleClass().add("property-search-row");
            HBox.setHgrow(itemView, Priority.ALWAYS);
        }

        public void setIconDisabled (boolean disabled) {
            icon.setDisable(disabled);
        }
    }

}
