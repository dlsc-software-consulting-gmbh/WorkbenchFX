package uk.co.senapt.desktop.shell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import uk.co.senapt.desktop.shell.skins.HorizontalMenuSkin;

/**
 * Created by gdiaz on 10/10/2017.
 */
public class HorizontalMenu<T> extends Control {

    public HorizontalMenu() {
        getStyleClass().add("horizontal-menu");

        showMenuButton.addListener(it -> requestLayout());

        setPrefWidth(0);
        setMinWidth(0);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new HorizontalMenuSkin<>(this);
    }

    private final ObservableList<T> items = FXCollections.observableArrayList();

    public final ObservableList<T> getItems () {
        return items;
    }

    // Selection model support.

    public final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>(this, "selectedItem");

    public final ObjectProperty<T> selectedItemProperty () {
        return selectedItem;
    }

    public final T getSelectedItem () {
        return selectedItemProperty().get();
    }

    public final void setSelectedItem (T selectedItem) {
        selectedItemProperty().set(selectedItem);
    }

    // Show menu button support.

    private final BooleanProperty showMenuButton = new SimpleBooleanProperty(this, "showMenuButton", true);

    public final BooleanProperty showMenuButtonProperty() {
        return showMenuButton;
    }

    public final void setShowMenuButton(boolean show) {
        this.showMenuButton.set(show);
    }

    public final boolean isShowMenuButton() {
        return showMenuButton.get();
    }

    // Show menu popup support.

    private final BooleanProperty showPopup = new SimpleBooleanProperty(this, "showPopup");

    public final BooleanProperty showPopupProperty () {
        return showPopup;
    }

    public final boolean isShowPopup () {
        return showPopupProperty().get();
    }

    public final void setShowPopup (boolean showPopup) {
        showPopupProperty().set(showPopup);
    }

    // Cell factory support.

    private final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory");

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return cellFactoryProperty().get();
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> cellFactory) {
        cellFactoryProperty().set(cellFactory);
    }

    // Popup cell factory support.

    private final ObjectProperty<Callback<ListView<T>, ListCell<T>>> popupCellFactory = new SimpleObjectProperty<>(this, "popupCellFactory");

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> popupCellFactoryProperty () {
        return popupCellFactory;
    }

    public final Callback<ListView<T>, ListCell<T>> getPopupCellFactory() {
        return popupCellFactoryProperty().get();
    }

    public final void setPopupCellFactory(Callback<ListView<T>, ListCell<T>> popupCellFactory) {
        popupCellFactoryProperty().set(popupCellFactory);
    }
}
