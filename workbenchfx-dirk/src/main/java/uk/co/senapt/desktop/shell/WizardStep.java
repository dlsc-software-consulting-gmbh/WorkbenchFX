package uk.co.senapt.desktop.shell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * Created by gdiaz on 25/09/2017.
 */
public class WizardStep {

    public WizardStep() {
        this(null);
    }

    public WizardStep(String name) {
        this(name, null);
    }

    public WizardStep(String name, Node content) {
        setName(name);
        setContent(content);
    }

    private final StringProperty name = new SimpleStringProperty(this, "name");

    public final StringProperty nameProperty() {
        return name;
    }

    public final String getName() {
        return nameProperty().get();
    }

    public final void setName(String name) {
        nameProperty().set(name);
    }

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>(this, "content");

    public final ObjectProperty<Node> contentProperty() {
        return content;
    }

    public final Node getContent() {
        return contentProperty().get();
    }

    public final void setContent(Node content) {
        contentProperty().set(content);
    }

    private final BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);

    public final BooleanProperty validProperty() {
        return valid;
    }

    public final boolean isValid() {
        return validProperty().get();
    }

    public final void setValid(boolean valid) {
        validProperty().set(valid);
    }

    private final ObservableList<Button> buttons = FXCollections.observableArrayList();

    public final ObservableList<Button> getButtons() {
        return buttons;
    }
}
