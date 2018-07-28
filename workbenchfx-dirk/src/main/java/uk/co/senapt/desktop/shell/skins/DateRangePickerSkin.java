package uk.co.senapt.desktop.shell.skins;

import java.time.LocalDate;
import java.time.format.FormatStyle;
import javafx.geometry.Bounds;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import uk.co.senapt.desktop.shell.DateRangePicker;
import uk.co.senapt.desktop.shell.model.LocalDateRange;

public class DateRangePickerSkin extends SkinBase<DateRangePicker> {

    private final Popup popup;
    private final TextField editor;
    private final StackPane button;

    public DateRangePickerSkin(DateRangePicker control) {
        super(control);

        popup = new Popup();
        popup.getContent().setAll(new DateRangePickerContent(control));
        popup.setAutoHide(true);

        LocalDateRangeStringConverter converter = new LocalDateRangeStringConverter();
        editor = new TextField();
        control.valueProperty().addListener(obs -> editor.setText(converter.toString(control.getValue())));

        // open button / arrow
        Region arrow = new Region();
        arrow.setFocusTraversable(false);
        arrow.getStyleClass().setAll("arrow");
        arrow.setId("arrow");
        arrow.setMaxWidth(Region.USE_PREF_SIZE);
        arrow.setMaxHeight(Region.USE_PREF_SIZE);
        arrow.setMouseTransparent(true);

        button = new StackPane();
        button.setFocusTraversable(false);
        button.setId("arrow-button");
        button.getStyleClass().setAll("arrow-button");
        button.getChildren().add(arrow);
        button.setOnMouseClicked(evt -> show());

        getChildren().addAll(editor, button);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double buttonWidth = button.prefWidth(-1);
        double editorWidth = contentWidth - buttonWidth;
        button.resizeRelocate(contentX + editorWidth, contentY, buttonWidth, contentHeight);
        editor.resizeRelocate(contentX, contentY, editorWidth, contentHeight);
    }

    private void show () {
        Bounds bounds = getSkinnable().localToScreen(getSkinnable().getBoundsInLocal());
        if (popup.isShowing()) {
            popup.setX(bounds.getMinX());
            popup.setY(bounds.getMinY() + getSkinnable().getHeight() + 1);
        } else {
            popup.show(getSkinnable(), bounds.getMinX(), bounds.getMinY() + getSkinnable().getHeight() + 1);
        }
    }

    private static class LocalDateRangeStringConverter extends StringConverter<LocalDateRange> {

        private final LocalDateStringConverter converter;
        private static final String SEPARATOR = " to ";

        LocalDateRangeStringConverter () {
            converter = new LocalDateStringConverter(FormatStyle.SHORT);
        }

        @Override
        public String toString(LocalDateRange range) {
            if (range != null) {
                return converter.toString(range.getStart()) + SEPARATOR + converter.toString(range.getEnd());
            }
            return null;
        }

        @Override
        public LocalDateRange fromString(String string) {
            if (string != null) {
                String[] s = string.split(SEPARATOR);
                if (s.length == 2) {
                    LocalDate start = converter.fromString(s[0]);
                    LocalDate end = converter.fromString(s[1]);
                    LocalDateRange range = new LocalDateRange();
                    range.setStart(start);
                    range.setEnd(end);
                    return range;
                }
            }
            return null;
        }
    }

    private static class DateRangePickerContent extends HBox {

        private final DatePicker startPicker;
        private final DatePicker endPicker;
        private final DateRangePicker picker;

        DateRangePickerContent (DateRangePicker picker) {
            this.picker = picker;
            startPicker = new DatePicker();
            endPicker = new DatePicker();

            startPicker.valueProperty().addListener(obs -> updateRange());
            startPicker.setDayCellFactory(dp -> new StartDateCell(endPicker));
            endPicker.valueProperty().addListener(obs -> updateRange());
            endPicker.setDayCellFactory(dp -> new EndDateCell(startPicker));

            getChildren().addAll(new DatePickerSkin(startPicker).getPopupContent(), new DatePickerSkin(endPicker).getPopupContent());
            getStyleClass().add("date-range-content");
        }

        private void updateRange () {
            LocalDateRange newRange = new LocalDateRange();
            newRange.setStart(startPicker.getValue());
            newRange.setEnd(endPicker.getValue());
            picker.setValue(newRange);
        }

    }

    private static class StartDateCell extends DateCell {

        private final DatePicker endPicker;

        StartDateCell(DatePicker endPicker) {
            this.endPicker = endPicker;
            this.endPicker.valueProperty().addListener(obs -> updateItem(getItem(), isEmpty()));
        }

        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            setDisable(empty || (item != null && endPicker.getValue() != null && (item.isEqual(endPicker.getValue()) || item.isAfter(endPicker.getValue()))));
        }
    }

    private static class EndDateCell extends DateCell {

        private final DatePicker startPicker;

        EndDateCell(DatePicker startPicker) {
            this.startPicker = startPicker;
            this.startPicker.valueProperty().addListener(obs -> updateItem(getItem(), isEmpty()));
        }

        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            setDisable(empty || (item != null && startPicker.getValue() != null && (item.isEqual(startPicker.getValue()) || item.isBefore(startPicker.getValue()))));
        }
    }

}
