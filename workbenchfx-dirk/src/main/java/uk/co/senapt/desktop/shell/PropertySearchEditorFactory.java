package uk.co.senapt.desktop.shell;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import uk.co.senapt.desktop.shell.model.PropertyDefinition;
import uk.co.senapt.desktop.shell.model.SearchOperator;
import uk.co.senapt.desktop.shell.model.SearchValue;

public class PropertySearchEditorFactory implements Callback<PropertySearchItemView, Node> {
    @Override
    public Node call(PropertySearchItemView field) {
        PropertyDefinition property = field.getProperty();
        SearchOperator operator = field.getOperator();

        if (property == null || operator == null || !operator.isRequiredValue()) {
            return null;
        }

        if (property.isFixedValues()) {
            ComboBox<SearchValue> combo = new ComboBox<>();
            combo.getItems().setAll(property.getFixedValues());
            combo.valueProperty().addListener(obs -> field.setValue(combo.getValue().getValue()));
            return combo;
        }

        switch (property.getType()) {
            case DATE:
                switch (operator){
                    case BETWEEN:
                    case NOT_BETWEEN:
                        DateRangePicker rangePicker = new DateRangePicker();
                        rangePicker.valueProperty().addListener(obs -> field.setValue(rangePicker.getValue()));
                        return rangePicker;

                    default:
                        DatePicker picker = new DatePicker();
                        picker.setEditable(false);
                        picker.valueProperty().addListener(obs -> field.setValue(picker.getValue()));
                        return picker;
                }
            case INTEGER:
                IntegerNumberField numberField = new IntegerNumberField();
                numberField.valueProperty().addListener(obs -> field.setValue(numberField.getValue()));
                return numberField;

            case DECIMAL:
                DecimalNumberField decimalField = new DecimalNumberField();
                decimalField.valueProperty().addListener(obs -> field.setValue(decimalField.getValue()));
                return decimalField;

            case YES_NO:
                ComboBox<Boolean> combo = new ComboBox<>();
                combo.getItems().add(Boolean.TRUE);
                combo.getItems().add(Boolean.FALSE);
                combo.valueProperty().addListener(obs -> field.setValue(combo.getValue()));
                return combo;

            default:
                TextField textField = new TextField();
                textField.textProperty().addListener(obs -> field.setValue(textField.getText()));
                return textField;
        }
    }
}
