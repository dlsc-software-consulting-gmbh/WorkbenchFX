package com.dlsc.workbenchfx.custom.patient.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.patient.model.FileCabinet;
import com.dlsc.workbenchfx.custom.patient.view.util.ViewMixin;

/**
 * @author Dieter Holz
 */
public class PatientListOverlay extends VBox implements ViewMixin {
    private final FileCabinet cabinet;
    private final Workbench workbench;

    private Label label;

    public PatientListOverlay(FileCabinet cabinet, Workbench workbench) {
        this.cabinet = cabinet;
        this.workbench = workbench;

        init();
    }

    @Override
    public void initializeSelf() {
        getStyleClass().addAll("patient-list-overlay");
        addStylesheetFiles("/com/dlsc/workbenchfx/custom/patient/css/patient.css");
    }

    @Override
    public void initializeParts() {
        label = new Label("hallo");
    }

    @Override
    public void layoutParts() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> workbench.hideOverlay(this));

        getChildren().addAll(label, closeButton);
    }
}
