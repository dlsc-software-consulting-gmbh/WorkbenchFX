package uk.co.senapt.desktop.shell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import uk.co.senapt.desktop.shell.model.LocalDateRange;
import uk.co.senapt.desktop.shell.skins.DateRangePickerSkin;

public class DateRangePicker extends Control {

    public DateRangePicker () {
        getStyleClass().add("date-picker");
        getStyleClass().add("date-range-picker");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DateRangePickerSkin(this);
    }

    private final ObjectProperty<LocalDateRange> value = new SimpleObjectProperty<>(this, "value");

    public final ObjectProperty<LocalDateRange> valueProperty () {
        return value;
    }

    public final LocalDateRange getValue () {
        return valueProperty().get();
    }

    public final void setValue (LocalDateRange value) {
        valueProperty().set(value);
    }

}
