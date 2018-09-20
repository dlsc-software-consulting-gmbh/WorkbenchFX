package com.dlsc.workbenchfx.modules.patient.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.modules.patient.model.FileCabinet;
import com.dlsc.workbenchfx.modules.patient.model.Translator;
import com.dlsc.workbenchfx.modules.patient.view.util.ViewMixin;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;

/**
 * @author Dieter Holz
 */
public class AllPatientsView extends BorderPane implements ViewMixin {

  private final FileCabinet cabinet;
  private final Translator translator;
  private final Workbench workbench;

  private Pagination pagination;

  public AllPatientsView(FileCabinet cabinet, Translator translator, Workbench workbench) {
    this.cabinet = cabinet;
    this.translator = translator;
    this.workbench = workbench;
    init();
  }

  @Override
  public void initializeSelf() {
    addStylesheetFiles("/fonts/fonts.css", "/com/dlsc/workbenchfx/modules/patient/css/patient.css");
  }

  @Override
  public void initializeParts() {
    pagination = new Pagination();
    pagination.getStyleClass().add("patient-pagination");
    pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
    pagination.pageCountProperty().bind(Bindings.size(cabinet.getAllPatients()));
    pagination.setPageFactory(
        param -> new PatientView(cabinet.getAllPatients().get(param), translator));
  }

  @Override
  public void setupValueChangedListeners() {
    cabinet.selectedPatientProperty()
        .addListener((observable, oldValue, newValue) -> pagination.setCurrentPageIndex(
            cabinet.getAllPatients().indexOf(newValue)));

    pagination.currentPageIndexProperty()
        .addListener((observable, oldValue, newValue) -> cabinet.setSelectedPatient(
            cabinet.getAllPatients().get(newValue.intValue())));
  }

  @Override
  public void layoutParts() {
    setCenter(pagination);
  }
}
