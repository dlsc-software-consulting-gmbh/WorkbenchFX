package com.dlsc.workbenchfx.custom.patient.view;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.patient.model.FileCabinet;
import com.dlsc.workbenchfx.custom.patient.view.util.ViewMixin;

/**
 * @author Dieter Holz
 */
public class PatientToolbar extends ToolBar implements ViewMixin {

    private static final String SAVE_ICON   = "\uf0c7";
    private static final String PLUS_ICON   = "\uf067";
    private static final String REMOVE_ICON = "\uf00d";
    private static final String USER_ICON = "\uf007";
    private final FileCabinet cabinet;
    private final Workbench workbench;

    private Button saveButton;
    private Button createButton;
    private Button deleteButton;
    private Button switchButton;

    public PatientToolbar(FileCabinet cabinet, Workbench workbench) {
        this.cabinet = cabinet;
        this.workbench = workbench;
        init();
    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("patient-toolbar");
    }

    @Override
    public void initializeParts() {
        saveButton = new Button(SAVE_ICON);
        saveButton.getStyleClass().add("fontawesome");

        createButton = new Button(PLUS_ICON);
        createButton.getStyleClass().add("fontawesome");

        deleteButton = new Button(REMOVE_ICON);
        deleteButton.getStyleClass().add("fontawesome");

        switchButton = new Button(USER_ICON);
        switchButton.getStyleClass().add("fontawesome");
    }

    @Override
    public void layoutParts() {
        getItems().addAll(saveButton, createButton, deleteButton, switchButton);
    }

    @Override
    public void setupEventHandlers() {
        saveButton.setOnAction(event -> cabinet.save());
        switchButton.setOnAction(event -> {
            workbench.showOverlay(new PatientListOverlay(cabinet, workbench), false);
        });
    }
}
