package uk.co.senapt.desktop.shell.model;

public class SearchValue {

    private String name;
    private Object value;

    public SearchValue (String name) {
        this.name = name;
        this.value = name;
    }

    public SearchValue (String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
