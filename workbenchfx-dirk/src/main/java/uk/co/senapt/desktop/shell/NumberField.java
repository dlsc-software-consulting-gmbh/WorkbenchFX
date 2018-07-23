package uk.co.senapt.desktop.shell;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.textfield.CustomTextField;

public abstract class NumberField<T extends Number> extends CustomTextField {

    boolean updatingFromText;
    private boolean allowDecimals;
    private int maxCharacters;
    private boolean keepTrailingZeros;

    NumberField (boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
        NumberStringFilteredConverter converter = new NumberStringFilteredConverter();
        setTextFormatter(new TextFormatter<>(converter, null, converter.getFilter()));
        textProperty().addListener(obs -> setValueFromString(getText()));
        valueProperty().addListener(obs -> setStringFromValue(getValue()));
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public void setKeepTrailingZeros(boolean keepTrailingZeros) {
        this.keepTrailingZeros = keepTrailingZeros;
    }

    protected abstract void setValueFromString (String text);

    private void setStringFromValue(T value) {
        if (!updatingFromText) {
            setText(value == null ? "" : String.valueOf(value));
        }
    }

    class NumberStringFilteredConverter extends NumberStringConverter {

        UnaryOperator<TextFormatter.Change> getFilter() {
            return change -> {
                String newText = change.getControlNewText();
                if (newText.isEmpty()) {
                    return change;
                }

                // Convert to number
                ParsePosition parsePosition = new ParsePosition( 0 );
                Object object = getNumberFormat().parse(newText, parsePosition);
                if (object == null || parsePosition.getIndex() < newText.length()) {
                    return null;
                }

                // Validate max length
                if (maxCharacters > 0 && newText.length() > maxCharacters) {
                    String head = change.getControlNewText().substring(0, maxCharacters);
                    change.setText(head);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }

                return change;
            };
        }

        @Override
        protected NumberFormat getNumberFormat() {
            NumberFormat format = allowDecimals ? super.getNumberFormat() : NumberFormat.getIntegerInstance();
            format.setGroupingUsed(false);
            return format;
        }

        @Override
        public String toString(Number value) {
            StringBuilder str = new StringBuilder();
            str.append(super.toString(value));

            if (keepTrailingZeros) {
                String text = getText();
                if (text != null && !text.isEmpty()) {
                    for (int i = 0; i < text.length(); i++) {
                        char c = text.charAt(i);
                        if (c == '0') {
                            str.insert(0, "0");
                        }
                    }
                }
            }

            return str.toString();
        }
    }

    // VALUE
    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");

    public final ObjectProperty<T> valueProperty () {
        return value;
    }

    public final T getValue () {
        return valueProperty().get();
    }

    public final void setValue (T value) {
        valueProperty().set(value);
    }

}
