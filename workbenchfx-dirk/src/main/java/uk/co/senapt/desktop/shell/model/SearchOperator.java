package uk.co.senapt.desktop.shell.model;

import java.util.ArrayList;
import java.util.List;

public enum SearchOperator {

    CONTAINS ("Contains") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.TEXT};
        }
    },

    NOT_CONTAINS ("Not Contains") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.TEXT};
        }
    },

    EQUALS ("Equals") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.TEXT, PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE, PropertyType.YES_NO, PropertyType.OBJECT};
        }
    },

    NOT_EQUALS ("Not Equals") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.TEXT, PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE, PropertyType.YES_NO, PropertyType.OBJECT};
        }
    },

    GREATER ("Greater") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE};
        }
    },

    GREATER_EQUAL ("Greater Equal") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE};
        }
    },

    LESS ("Less") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE};
        }
    },

    LESS_EQUAL ("Less Equal") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE};
        }
    },

    EMPTY ("Empty") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.TEXT, PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE, PropertyType.YES_NO, PropertyType.OBJECT};
        }

        @Override
        public boolean isRequiredValue() {
            return false;
        }
    },

    NOT_EMPTY ("Not Empty") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.TEXT, PropertyType.DECIMAL, PropertyType.INTEGER, PropertyType.DATE, PropertyType.YES_NO, PropertyType.OBJECT};
        }

        @Override
        public boolean isRequiredValue() {
            return false;
        }
    },

    BETWEEN ("Between") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.DATE};
        }
    },

    NOT_BETWEEN ("Not Between") {
        @Override
        public PropertyType[] getSupportedTypes() {
            return new PropertyType[] {PropertyType.DATE};
        }
    };

    private String name;

    SearchOperator (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSupportedType (PropertyType propertyType) {
        for (PropertyType type : getSupportedTypes()) {
            if (type == propertyType) {
                return true;
            }
        }
        return false;
    }

    public abstract PropertyType[] getSupportedTypes ();

    public boolean isRequiredValue () {
        return true;
    }

    public static List<SearchOperator> getSupportedOperators (PropertyType propertyType) {
        List<SearchOperator> operators = new ArrayList<>();
        for (SearchOperator operator : values()) {
            if (operator.isSupportedType(propertyType)) {
                operators.add(operator);
            }
        }
        return operators;
    }

    public static SearchOperator fromName (String name) {
        if (name != null) {
            for (SearchOperator val : values()) {
                if (val.name.equals(name)) {
                    return val;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
