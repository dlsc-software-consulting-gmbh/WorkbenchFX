package com.dlsc.workbenchfx.modules.patient.view;

import com.dlsc.workbenchfx.modules.patient.model.FileCabinet;
import com.dlsc.workbenchfx.modules.patient.model.Patient;
import com.dlsc.workbenchfx.modules.patient.model.Translator;
import com.dlsc.workbenchfx.modules.patient.view.util.ViewMixin;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * @author Dieter Holz
 */
public class PatientSelectionView extends VBox implements ViewMixin {

  private final FileCabinet cabinet;
  private final Translator translator;

  private TableView<Patient> patientTable;
  private TextField filter;

  private FilteredList<Patient> filteredPatients;

  public PatientSelectionView(FileCabinet cabinet, Translator translator) {
    this.cabinet = cabinet;
    this.translator = translator;

    init();
  }

  @Override
  public void initializeSelf() {
    filteredPatients = new FilteredList<>(cabinet.getAllPatients());
  }

  @Override
  public void initializeParts() {

    patientTable = new TableView<>(filteredPatients);

    TableColumn<Patient, String> firstNameCol = new TableColumn<>();
    firstNameCol.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    firstNameCol.textProperty().bind(translator.firstNameLabelProperty());

    TableColumn<Patient, String> lastNameCol = new TableColumn<>();
    lastNameCol.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    lastNameCol.textProperty().bind(translator.lastNameLabelProperty());

    TableColumn<Patient, String> yearOfBirthCol = new TableColumn<>();
    yearOfBirthCol.setCellValueFactory(cell -> cell.getValue().yearOfBirthProperty().asString());
    yearOfBirthCol.textProperty().bind(translator.yearOfBirthLabelProperty());

    patientTable.getColumns().addAll(firstNameCol, lastNameCol, yearOfBirthCol);

    filter = new TextField();

  }

  @Override
  public void layoutParts() {
    setSpacing(10);
    getChildren().addAll(filter, patientTable);
  }

  public Patient getSelected() {
    return patientTable.getSelectionModel().getSelectedItem();
  }
}
