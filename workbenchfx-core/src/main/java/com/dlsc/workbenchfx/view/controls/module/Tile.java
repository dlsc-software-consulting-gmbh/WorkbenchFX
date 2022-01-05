package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.util.WorkbenchUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the standard control used to display {@link WorkbenchModule}s as tiles
 * in the add module screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tile extends Control {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tile.class.getName());

    private final Workbench workbench;
    private final ObjectProperty<WorkbenchModule> module;

    private final StringProperty name;
    private final ObjectProperty<Node> icon;

    /**
     * Constructs a new {@link Tile}.
     *
     * @param workbench which created this {@link Tile}
     */
    public Tile(Workbench workbench) {
        this.workbench = workbench;
        module = new SimpleObjectProperty<>(this, "module");
        name = new SimpleStringProperty(this, "name");
        icon = new SimpleObjectProperty<>(this, "icon");
        setupModuleListeners();
        setupEventHandlers();
        getStyleClass().add("tile-control");
    }

    private void setupModuleListeners() {
        module.addListener(observable -> {
            WorkbenchModule current = getModule();
            name.setValue(current.getName());
            icon.setValue(current.getIcon());

            // Sets the id with toString of module.
            // Adds 'tile-', replaces spaces with hyphens and sets letters to lowercase.
            // eg. Customer Management converts to tile-customer-management
            String tileId = WorkbenchUtils.convertToId("tile-" + current.getName());
            LOGGER.debug("Set Tile-ID of '" + getModule() + "' to: '" + tileId + "'");
            setId(tileId);
        });
    }

    private void setupEventHandlers() {
        setOnMouseClicked(event -> open());
    }

    /**
     * Opens the {@link WorkbenchModule} belonging to this {@link Tile}.
     */
    public final void open() {
        workbench.openModule(getModule());
    }

    public final WorkbenchModule getModule() {
        return module.get();
    }

    /**
     * Defines the {@code module} which is being represented by this {@link Tile}.
     *
     * @param module to be represented by this {@link Tile}
     */
    public final void setModule(WorkbenchModule module) {
        LOGGER.trace("Setting reference to module");
        this.module.set(module);
    }

    public final ObjectProperty<WorkbenchModule> moduleProperty() {
        return module;
    }

    public final String getName() {
        return name.get();
    }

    public final ReadOnlyStringProperty nameProperty() {
        return name;
    }

    public final Node getIcon() {
        return icon.get();
    }

    public final ReadOnlyObjectProperty<Node> iconProperty() {
        return icon;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TileSkin(this);
    }
}
