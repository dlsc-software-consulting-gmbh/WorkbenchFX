package com.dlsc.workbenchfx.custom.patient.view;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.patient.model.FileCabinet;
import com.dlsc.workbenchfx.custom.patient.view.util.ViewMixin;

/**
 * @author Dieter Holz
 */
public class PatientView extends BorderPane implements ViewMixin {

    private final FileCabinet cabinet;
    private final Workbench workbench;

    private PatientToolbar toolBar;
    private Pagination     pagination;

    public PatientView(FileCabinet cabinet, Workbench workbench) {
        this.cabinet = cabinet;
        this.workbench = workbench;
        init();
    }

    @Override
    public void initializeSelf() {
        addStylesheetFiles("/fonts/fonts.css", "/com/dlsc/workbenchfx/custom/patient/css/patient.css");
    }

    @Override
    public void initializeParts() {
        toolBar = new PatientToolbar(cabinet, workbench);

        pagination = new Pagination();
        pagination.getStyleClass().add("patient-pagination");
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.pageCountProperty().bind(Bindings.size(cabinet.getAllPatients()));
        pagination.setPageFactory(param -> new DetailView(cabinet.getAllPatients().get(param)));

    }

    @Override
    public void layoutParts() {
        setTop(toolBar);
        setCenter(pagination);
    }
}
