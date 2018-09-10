package com.dlsc.workbenchfx.modules.patient.view;

import com.dlsc.workbenchfx.modules.patient.model.Patient;
import com.dlsc.workbenchfx.modules.patient.model.Translator;
import com.dlsc.workbenchfx.modules.patient.view.util.MaterialDesign;
import com.dlsc.workbenchfx.modules.patient.view.util.ViewMixin;
import com.dlsc.workbenchfx.modules.patient.view.util.numberrange.NumberRangeControl;
import com.dlsc.workbenchfx.modules.patient.view.util.numberrange.SkinType;
import com.dlsc.workbenchfx.modules.patient.view.util.rectangularimageview.RectangularImageView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Dieter Holz
 */
class PatientView extends HBox implements ViewMixin {

  private final Patient patient;
  private final Translator translator;

  private TextField firstNameField;
  private TextField lastNameField;

  private TextField yearOfBirthField;
  private TextField genderField;

  private RectangularImageView imageView;
  private TextField imgURLField;

  private NumberRangeControl bloodPressureSystolicControl;
  private NumberRangeControl bloodPressureDiastolicControl;
  private NumberRangeControl weightControl;
  private NumberRangeControl tallnessControl;

  PatientView(Patient patient, Translator translator) {
    this.patient = patient;
    this.translator = translator;

    init();
  }

  @Override
  public void initializeSelf() {
    getStyleClass().add("detail-view");
    setPadding(new Insets(10));
  }

  @Override
  public void initializeParts() {
    firstNameField = new TextField();
    firstNameField.getStyleClass().add("heading");

    lastNameField = new TextField();
    lastNameField.getStyleClass().add("heading");

    yearOfBirthField = new TextField();
    yearOfBirthField.getStyleClass().add("subheading");

    genderField = new TextField();
    genderField.getStyleClass().add("subheading");

    imageView = new RectangularImageView();
    imgURLField = new TextField();
    imgURLField.getStyleClass().add("tiny");

    bloodPressureSystolicControl = new NumberRangeControl(SkinType.SLIM);
    bloodPressureSystolicControl.setTitle("Systolic");
    bloodPressureSystolicControl.setUnit("mmHg");
    bloodPressureSystolicControl.setInteractive(true);
    bloodPressureSystolicControl.setMinValue(40);
    bloodPressureSystolicControl.setMaxValue(220);

    bloodPressureDiastolicControl = new NumberRangeControl(SkinType.SLIM);
    bloodPressureDiastolicControl.setTitle("Diastolic");
    bloodPressureDiastolicControl.setUnit("mmHg");
    bloodPressureDiastolicControl.setInteractive(true);
    bloodPressureDiastolicControl.setMinValue(40);
    bloodPressureDiastolicControl.setMaxValue(220);

    weightControl = new NumberRangeControl(SkinType.SLIM);
    weightControl.setTitle("Weight");
    weightControl.setUnit("kg");
    weightControl.setInteractive(true);
    weightControl.setMinValue(0);
    weightControl.setMaxValue(250);

    tallnessControl = new NumberRangeControl(SkinType.SLIM);
    tallnessControl.setTitle("Tall");
    tallnessControl.setUnit("cm");
    tallnessControl.setInteractive(true);
    tallnessControl.setMinValue(30);
    tallnessControl.setMaxValue(250);


    Platform.runLater(() -> {
      bloodPressureSystolicControl.setBaseColor(MaterialDesign.CYAN_300.getColor());
      bloodPressureDiastolicControl.setBaseColor(MaterialDesign.CYAN_300.getColor());
      weightControl.setBaseColor(MaterialDesign.RED_300.getColor());
      tallnessControl.setBaseColor(MaterialDesign.ORANGE_300.getColor());
    });
  }

  @Override
  public void layoutParts() {
    setSpacing(20);

    GridPane dashboard = new GridPane();
    RowConstraints growingRow = new RowConstraints();
    growingRow.setVgrow(Priority.ALWAYS);
    ColumnConstraints growingCol = new ColumnConstraints();
    growingCol.setHgrow(Priority.ALWAYS);
    dashboard.getRowConstraints().setAll(growingRow, growingRow);
    dashboard.getColumnConstraints().setAll(growingCol, growingCol);

    dashboard.setVgap(60);

    dashboard.setPrefHeight(800);
    dashboard.addRow(0, bloodPressureSystolicControl, bloodPressureDiastolicControl);
    dashboard.addRow(1, weightControl, tallnessControl);


    setHgrow(dashboard, Priority.ALWAYS);

    GridPane form = new GridPane();
    form.setHgap(10);
    form.setVgap(25);
    form.setMaxWidth(410);

    GridPane.setVgrow(imageView, Priority.ALWAYS);
    GridPane.setValignment(imageView, VPos.BOTTOM);

    form.add(firstNameField, 0, 0);
    form.add(lastNameField, 1, 0);
    form.add(yearOfBirthField, 0, 1);
    form.add(genderField, 1, 1);
    form.add(imageView, 0, 2, 2, 1);
    form.add(imgURLField, 0, 3, 2, 1);

    getChildren().addAll(form, dashboard);

  }

  @Override
  public void setupBindings() {
    firstNameField.textProperty().bindBidirectional(patient.firstNameProperty());
    lastNameField.textProperty().bindBidirectional(patient.lastNameProperty());

    yearOfBirthField.textProperty().bindBidirectional(patient.yearOfBirthProperty(),
        new NumberStringConverter() {
          @Override
          public Number fromString(String value) {
            try {
              return super.fromString(value);
            } catch (Exception e) {
              return patient.getYearOfBirth();
            }
          }

          @Override
          public String toString(Number value) {
            return String.format("%d", value.intValue());
          }
        });

    genderField.textProperty().bindBidirectional(patient.genderProperty(),
        new StringConverter<Patient.Gender>() {
          @Override
          public String toString(Patient.Gender gender) {
            return gender.name().toLowerCase();
          }

          @Override
          public Patient.Gender fromString(String string) {
            return Patient.Gender.valueOf(string.toUpperCase());
          }
        });

    bloodPressureSystolicControl.valueProperty().bindBidirectional(
        patient.bloodPressureSystolicProperty());
    bloodPressureDiastolicControl.valueProperty().bindBidirectional(
        patient.bloodPressureDiastolicProperty());
    weightControl.valueProperty().bindBidirectional(patient.weightProperty());
    tallnessControl.valueProperty().bindBidirectional(patient.tallnessProperty());

    imageView.imageURLProperty().bindBidirectional(patient.imageURLProperty());
    imgURLField.textProperty().bindBidirectional(patient.imageURLProperty());

    bloodPressureSystolicControl.titleProperty().bind(
        translator.bloodPressureSystolicLabelProperty());
    bloodPressureDiastolicControl.titleProperty().bind(
        translator.bloodPressureDiastolicLabelProperty());
    weightControl.titleProperty().bind(translator.weightLabelProperty());
    tallnessControl.titleProperty().bind(translator.tallnessLabelProperty());
  }
}
