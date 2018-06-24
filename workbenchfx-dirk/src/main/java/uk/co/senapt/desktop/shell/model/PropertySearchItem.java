package uk.co.senapt.desktop.shell.model;

public class PropertySearchItem {

    private PropertyDefinition property;
    private SearchOperator operator;
    private Object value;

    public PropertyDefinition getProperty() {
        return property;
    }

    public void setProperty(PropertyDefinition property) {
        this.property = property;
    }

    public SearchOperator getOperator() {
        return operator;
    }

    public void setOperator(SearchOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
