package uk.co.senapt.desktop.shell;

import com.google.inject.Module;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Created by lemmi on 18.08.17.
 */
public abstract class ShellModule<T extends Shell> {

    private String name;

    protected ShellModule(String name) {
        this.name = Objects.requireNonNull(name);
    }

    protected List<? extends Module> getContextModules() {
        return Collections.emptyList();
    }

    protected String getIconClassBaseName() {
        return "customer-management";
    }

    public String getIconClass() {
        return getIconClassBaseName() + "-icon";
    }

    public String getMenuIconClass() {
        return getIconClassBaseName() + "-icon-menu";
    }

    public String getTaskbarIconClass() {
        return getIconClassBaseName() + "-icon-taskbar";
    }

    public String getName() {
        return name;
    }

    public Region getPane(T shell) {
        Label label = new Label("\"" + name + "\" UI has not been implemented, yet!");
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.getStyleClass().add("missing-module-ui");
        return label;
    }

    public abstract String getPrimaryColorStyleClass();

    public List<String> getModuleStylesheets() {
        return Collections.emptyList();
    }

    public boolean isImplemented() {
        return false;
    }
}
