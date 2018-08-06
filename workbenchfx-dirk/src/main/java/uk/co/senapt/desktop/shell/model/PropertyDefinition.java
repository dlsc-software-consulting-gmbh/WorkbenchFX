package uk.co.senapt.desktop.shell.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyDefinition {

    public static final String DEFAULT_CATEGORY = "GENERAL";

    private String name;
    private String category;
    private PropertyType type;
    private List<SearchValue> fixedValues = new ArrayList<>();

    public PropertyDefinition(String name) {
        this(name, PropertyType.TEXT);
    }

    public PropertyDefinition(String name, String category) {
        this(name, category, PropertyType.TEXT);
    }

    public PropertyDefinition(String name, PropertyType type) {
        this(name, DEFAULT_CATEGORY, type);
    }

    public PropertyDefinition(String name, String category, PropertyType type) {
        this.name = name;
        this.category = category;
        this.type = type;
    }

    public PropertyDefinition (String name, SearchValue... fixedValues) {
        this(name, DEFAULT_CATEGORY, PropertyType.OBJECT);
        if (fixedValues != null) {
            for (SearchValue val : fixedValues) {
                this.fixedValues.add(val);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<SearchValue> getFixedValues() {
        return fixedValues;
    }

    public void setFixedValues(List<SearchValue> fixedValues) {
        this.fixedValues = fixedValues;
        if (fixedValues != null && !fixedValues.isEmpty()) {
            type = PropertyType.OBJECT;
        }
    }

    public boolean isFixedValues () {
        return fixedValues != null && !fixedValues.isEmpty();
    }

    public void addFixedValue (SearchValue fixedValue) {
        if (fixedValues == null) {
            fixedValues = new ArrayList<>();
        }
        fixedValues.add(fixedValue);
        type = PropertyType.OBJECT;
    }

    @Override
    public String toString() {
        return name;
    }
}
