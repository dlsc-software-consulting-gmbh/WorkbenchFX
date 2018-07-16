package com.dlsc.workbenchfx.custom.patient.view;

import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.patient.model.FileCabinet;
import com.dlsc.workbenchfx.custom.patient.model.Patient;
import com.dlsc.workbenchfx.custom.patient.model.Translator;
import com.dlsc.workbenchfx.custom.patient.view.util.ViewMixin;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;

/**
 * @author Dieter Holz
 */
public class PatientToolbar extends ToolBar implements ViewMixin {

    private static final String SAVE_ICON   = "\uf0c7";
    private static final String PLUS_ICON   = "\uf067";
    private static final String REMOVE_ICON = "\uf00d";
    private static final String USER_ICON   = "\uf007";

    private final FileCabinet cabinet;
    private final Translator  translator;
    private final Workbench   workbench;

    private Button saveButton;
    private Button createButton;
    private Button deleteButton;
    private Button switchButton;

    private TableView<Patient> patientTable;
    private TextField          filter;

    private WorkbenchDialog selectPatientDialog;

    private FilteredList<Patient> filteredPatients;

    public PatientToolbar(FileCabinet cabinet, Translator translator, Workbench workbench) {
        this.cabinet = cabinet;
        this.translator = translator;
        this.workbench = workbench;
        init();
    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("patient-toolbar");

        filteredPatients = new FilteredList<>(cabinet.getAllPatients());
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

        VBox dialogContent = new VBox(filter, patientTable);
        dialogContent.setSpacing(10);

        selectPatientDialog = WorkbenchDialog.builder("Select Patient", dialogContent, WorkbenchDialog.Type.INPUT)
                                             .build();

        selectPatientDialog.titleProperty().bind(translator.selectPatientLabelProperty());
      //  selectPatientDialog.cancelButtonTextProperty().bind()
    }

    @Override
    public void layoutParts() {
        getItems().addAll(saveButton, createButton, deleteButton, switchButton);
    }

    @Override
    public void setupEventHandlers() {
        saveButton.setOnAction(event -> cabinet.save());

        switchButton.setOnAction(event -> {
            DialogControl dialogControl = workbench.showDialog(selectPatientDialog);

            CompletableFuture<ButtonType> result = dialogControl.getDialog().getResult();
            result.thenAccept(buttonType -> {

                if (buttonType.equals(ButtonType.CANCEL)) {
                    //cancel
                } else {
                    cabinet.setSelectedPatient(patientTable.getSelectionModel().getSelectedItem());
                }
            });

            dialogControl.setOnShown(evt -> {
                filter.setText("");
                dialogControl.setButtonTextUppercase(false);
//                ((Button) dialogControl.getButtons().get(1)).textProperty().bind(translator.cancelLabelProperty());
//                ((Button) dialogControl.getButtons().get(0)).textProperty().bind(translator.okLabelProperty());
            });

        });

    }

    @Override
    public void setupValueChangedListeners() {
        cabinet.selectedPatientProperty().addListener((observable, oldValue, newValue) -> {
            patientTable.getSelectionModel().select(newValue);
        });

        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPatients.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return patient.getFirstName().toLowerCase().startsWith(newValue.toLowerCase()) ||
                       patient.getLastName().toLowerCase().startsWith(newValue.toLowerCase()) ||
                       String.valueOf(patient.getYearOfBirth()).equals(newValue);
            });
        });
    }
}
