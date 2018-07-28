package uk.co.senapt.desktop.shell;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.SizeConverter;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import uk.co.senapt.desktop.shell.model.PropertyDefinition;
import uk.co.senapt.desktop.shell.model.PropertySearchItem;
import uk.co.senapt.desktop.shell.model.PropertyType;
import uk.co.senapt.desktop.shell.model.SearchOperator;
import uk.co.senapt.desktop.shell.skins.PropertySearchItemViewSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertySearchItemView extends Control {

    private static final PropertySearchEditorFactory DEFAULT_EDITOR_FACTORY = new PropertySearchEditorFactory();

    public PropertySearchItemView() {
        getStyleClass().add("property-search-item");
        empty.bind(Bindings.isNull(item));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertySearchItemViewSkin(this);
    }

    // PROPERTY
    private final ObjectProperty<PropertyDefinition> property = new SimpleObjectProperty<PropertyDefinition>(this, "property") {
        @Override
        protected void invalidated() {
            propertyType.set(getValue() == null ? null : getValue().getType());
            if (getItem() != null) {
                getItem().setProperty(getValue());
            }
            PropertySearchItemView.this.setValue(null);
        }
    };

    // PROPERTY TYPE
    private final ReadOnlyObjectWrapper<PropertyType> propertyType = new ReadOnlyObjectWrapper<>(this, "propertyType");

    public final ReadOnlyObjectProperty<PropertyType> propertyTypeProperty() {
        return propertyType.getReadOnlyProperty();
    }

    public final PropertyType getPropertyType () {
        return propertyType.get();
    }

    public final ObjectProperty<PropertyDefinition> propertyProperty() {
        return property;
    }

    public final PropertyDefinition getProperty () {
        return propertyProperty().get();
    }

    public final void setProperty (PropertyDefinition property) {
        propertyProperty().set(property);
    }

    // OPERATOR
    private final ObjectProperty<SearchOperator> operator = new SimpleObjectProperty<SearchOperator>(this, "operator") {
        @Override
        protected void invalidated() {
            if (getItem() != null) {
                getItem().setOperator(getValue());
            }
            PropertySearchItemView.this.setValue(null);
        }
    };

    public final ObjectProperty<SearchOperator> operatorProperty () {
        return operator;
    }

    public final SearchOperator getOperator () {
        return operatorProperty().get();
    }

    public final void setOperator (SearchOperator operator) {
        operatorProperty().set(operator);
    }

    // VALUE
    private final ObjectProperty<Object> value = new SimpleObjectProperty<Object>(this, "value") {
        @Override
        protected void invalidated() {
            if (getItem() != null) {
                getItem().setValue(getValue());
            }
        }
    };

    public final ObjectProperty<Object> valueProperty() {
        return value;
    }

    public final Object getValue() {
        return valueProperty().get();
    }

    public final void setValue(Object value) {
        valueProperty().set(value);
    }

    // EDITOR FACTORY
    private ObjectProperty<Callback<PropertySearchItemView, Node>> editorFactory;

    public final ObjectProperty<Callback<PropertySearchItemView, Node>> editorFactoryProperty() {
        if (editorFactory == null) {
            editorFactory = new SimpleObjectProperty<>(this, "editorFactory", DEFAULT_EDITOR_FACTORY);
        }
        return editorFactory;
    }

    public final Callback<PropertySearchItemView, Node> getEditorFactory() {
        return editorFactoryProperty().get();
    }

    public void setEditorFactory(Callback<PropertySearchItemView, Node> editorFactory) {
        editorFactoryProperty().set(editorFactory);
    }

    // AVAILABLE PROPERTIES
    private final ObservableList<PropertyDefinition> availableProperties = FXCollections.observableArrayList();

    public final ObservableList<PropertyDefinition> getAvailableProperties() {
        return availableProperties;
    }

    // EMPTY
    private final ReadOnlyBooleanWrapper empty = new ReadOnlyBooleanWrapper(this, "empty", true);

    public final ReadOnlyBooleanProperty emptyProperty () {
        return empty.getReadOnlyProperty();
    }

    public final boolean isEmpty () {
        return empty.get();
    }

    // ITEM
    private final ObjectProperty<PropertySearchItem> item = new SimpleObjectProperty<PropertySearchItem>(this, "item") {
        @Override
        public void set(PropertySearchItem newValue) {
            super.set(newValue);
            if (newValue == null) {
                PropertySearchItemView.this.setValue(null);
                PropertySearchItemView.this.setOperator(null);
                PropertySearchItemView.this.setProperty(null);
            }
            else {
                PropertySearchItemView.this.setProperty(newValue.getProperty());
                PropertySearchItemView.this.setOperator(newValue.getOperator());
                PropertySearchItemView.this.setValue(newValue.getValue());
            }
        }
    };

    public final ObjectProperty<PropertySearchItem> itemProperty () {
        return item;
    }

    public final PropertySearchItem getItem () {
        return itemProperty().get();
    }

    public final void setItem (PropertySearchItem item) {
        itemProperty().set(item);
    }

    // HGAP
    private DoubleProperty hgap;
    public final DoubleProperty hgapProperty() {
        if (hgap == null) {
            hgap = new StyleableDoubleProperty(0) {
                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public CssMetaData<PropertySearchItemView, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override
                public Object getBean() {
                    return PropertySearchItemView.this;
                }

                @Override
                public String getName() {
                    return "hgap";
                }
            };
        }
        return hgap;
    }

    public final void setHgap(double value) { hgapProperty().set(value); }
    public final double getHgap() { return hgap == null ? 0 : hgap.get(); }

    // CSS
    private static class StyleableProperties {
        private static final CssMetaData<PropertySearchItemView,Number> HGAP =
            new CssMetaData<PropertySearchItemView,Number>("-fx-hgap", SizeConverter.getInstance(), 0.0){
                @Override
                public boolean isSettable(PropertySearchItemView node) {
                    return node.hgap == null || !node.hgap.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(PropertySearchItemView node) {
                    return (StyleableProperty<Number>) node.hgapProperty();
                }
            };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(HGAP);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }


    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

}
