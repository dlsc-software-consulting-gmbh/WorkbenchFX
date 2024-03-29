package com.dlsc.workbenchfx.demo.modules.patient.view;

import com.dlsc.workbenchfx.demo.modules.patient.model.FileCabinet;
import com.dlsc.workbenchfx.demo.modules.patient.model.Translator;
import com.dlsc.workbenchfx.demo.modules.patient.view.util.ViewMixin;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;

/**
 * @author Dieter Holz
 */
public class AllPatientsView extends BorderPane implements ViewMixin {

  private final FileCabinet cabinet;
  private final Translator translator;

  private Pagination pagination;

  public AllPatientsView(FileCabinet cabinet, Translator translator) {
    this.cabinet = cabinet;
    this.translator = translator;
    init();
  }

  @Override
  public void initializeSelf() {
    addStylesheetFiles("/fonts/fonts.css", "/com/dlsc/workbenchfx/demo/modules/patient/css/patient.css");
  }

  @Override
  public void initializeParts() {
    pagination = new Pagination();
    pagination.getStyleClass().add("patient-pagination");
    pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
    pagination.pageCountProperty().bind(Bindings.size(cabinet.getAllPatients()));
    pagination.setPageFactory(
        param -> new com.dlsc.workbenchfx.demo.modules.patient.view.PatientView(cabinet.getAllPatients().get(param), translator));
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
