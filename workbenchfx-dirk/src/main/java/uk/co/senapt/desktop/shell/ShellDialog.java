package uk.co.senapt.desktop.shell;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;

public final class ShellDialog<T> {

    public enum Type {
        INPUT,
        INFORMATION,
        ERROR,
        WARNING,
        CONFIRMATION
    }

    private Type type;

    public ShellDialog(Type type) {
        this.type = type;

        getStyleClass().add(type.name().toLowerCase());

        switch (type) {
            case INPUT:
                getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                break;
            case INFORMATION:
                getButtonTypes().setAll(ButtonType.OK);
                break;
            case ERROR:
                getButtonTypes().setAll(ButtonType.CLOSE);
                break;
            case WARNING:
                getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                break;
            case CONFIRMATION:
                getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                break;
        }
    }

    public final Type getType() {
        return type;
    }

    private final CompletableFuture<T> result = new CompletableFuture<>();

    public final CompletableFuture<T> getResult() {
        return result;
    }

    // button types

    private ObservableList<ButtonType> buttonTypes = FXCollections.observableArrayList();

    public ObservableList<ButtonType> getButtonTypes() {
        return buttonTypes;
    }

    // maximize

    private final BooleanProperty maximize = new SimpleBooleanProperty();

    public final BooleanProperty maximizeProperty() {
        return maximize;
    }

    public final void setMaximize(boolean max) {
        maximize.set(max);
    }

    public final boolean isMaximize() {
        return maximize.get();
    }

    private final ObjectProperty<Callable> onCancelled = new SimpleObjectProperty<>(this, "onCancelled");

    public Callable getOnCancelled() {
        return onCancelled.get();
    }

    // content

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>(this, "content");

    public final ObjectProperty<Node> contentProperty() {
        return content;
    }

    public void setContent(Node content) {
        this.content.set(content);
    }

    public Node getContent() {
        return content.get();
    }

    // title

    private final StringProperty title = new SimpleStringProperty(this, "title", "Dialog");

    public final StringProperty titleProperty() {
        return title;
    }

    public final String getTitle() {
        return title.get();
    }

    public final void setTitle(String title) {
        this.title.set(title);
    }

    // custom style

    private final ObservableList<String> styleClass = FXCollections.observableArrayList();

    public ObservableList<String> getStyleClass() {
        return styleClass;
    }


    // Show buttons bar
    private final BooleanProperty showButtonsBar = new SimpleBooleanProperty(this, "showButtonsBar", true);

    public final BooleanProperty showButtonsBarProperty () {
        return showButtonsBar;
    }

    public final boolean isShowButtonsBar () {
        return showButtonsBarProperty().get();
    }

    public final void setShowButtonsBar (boolean showButtonsBar) {
        showButtonsBarProperty().set(showButtonsBar);
    }

    // exception

    private final ObjectProperty<Exception> exception = new SimpleObjectProperty<>(this, "exception");

    public final ObjectProperty<Exception> exceptionProperty() {
        return exception;
    }

    public final void setException(Exception ex) {
        this.exception.set(ex);
    }

    public final Exception getException() {
        return exception.get();
    }
}
