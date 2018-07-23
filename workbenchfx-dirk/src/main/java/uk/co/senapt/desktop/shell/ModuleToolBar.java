package uk.co.senapt.desktop.shell;

import java.util.Objects;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Created by lemmi on 29.08.17.
 */
public abstract class ModuleToolBar<T extends ShellModule> extends HBox {

    private final T module;

    public ModuleToolBar(T module) {
        this.module = Objects.requireNonNull(module);
        getStyleClass().addAll("module-toolbar", module.getPrimaryColorStyleClass());

        Button minimizeButton = new Button();
        Button closeButton = new Button();

        minimizeButton.getStyleClass().addAll("minimize-button", "minimize-icon");
        closeButton.getStyleClass().addAll("close-button", "close-icon");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        Region controls = createControls();
        HBox.setHgrow(controls, Priority.ALWAYS);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(separator, minimizeButton, closeButton);
        buttons.getStyleClass().add("buttons");

        getChildren().addAll(controls, buttons);
    }

    public T getModule() {
        return module;
    }

    protected abstract Region createControls();
}
