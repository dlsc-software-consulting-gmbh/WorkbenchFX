package com.dlsc.workbenchfx.modules.patient.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dieter Holz
 */
public class Translator {
  private StringProperty idLabel = new SimpleStringProperty();
  private StringProperty firstNameLabel = new SimpleStringProperty();
  private StringProperty lastNameLabel = new SimpleStringProperty();
  private StringProperty bloodPressureSystolicLabel = new SimpleStringProperty();
  private StringProperty bloodPressureDiastolicLabel = new SimpleStringProperty();
  private StringProperty weightLabel = new SimpleStringProperty();
  private StringProperty tallnessLabel = new SimpleStringProperty();
  private StringProperty yearOfBirthLabel = new SimpleStringProperty();
  private StringProperty genderLabel = new SimpleStringProperty();
  private StringProperty imageURLLabel = new SimpleStringProperty();

  private StringProperty selectPatientLabel = new SimpleStringProperty();
  private StringProperty okLabel = new SimpleStringProperty();
  private StringProperty cancelLabel = new SimpleStringProperty();

  public Translator() {
    translateToEnglish();
  }

  public void translateToGerman() {
    setIdLabel("Patienten Nr.");
    setFirstNameLabel("Vorname");
    setLastNameLabel("Nachname");
    setBloodPressureSystolicLabel("Systolisch");
    setBloodPressureDiastolicLabel("Diastolisch");
    setWeightLabel("Gewicht");
    setTallnessLabel("Grösse");
    setYearOfBirthLabel("Geburtsjahr");
    setGenderLabel("Geschlecht");
    setImageURLLabel("Bild URL");

    setSelectPatientLabel("Patient ausw\u00e4hlen");
    setOkLabel("OK");
    setCancelLabel("Abbrechen");
  }

  public void translateToEnglish() {
    setIdLabel("Patient No.");
    setFirstNameLabel("First Name");
    setLastNameLabel("Last Name");
    setBloodPressureSystolicLabel("Systolic");
    setBloodPressureDiastolicLabel("Diastolic");
    setWeightLabel("Weight");
    setTallnessLabel("Tall");
    setYearOfBirthLabel("Year of Birth");
    setGenderLabel("Gender");
    setImageURLLabel("Image URL");

    setSelectPatientLabel("Select patient");
    setOkLabel("OK");
    setCancelLabel("Cancel");
  }

  public String getIdLabel() {
    return idLabel.get();
  }

  public StringProperty idLabelProperty() {
    return idLabel;
  }

  public void setIdLabel(String idLabel) {
    this.idLabel.set(idLabel);
  }

  public String getFirstNameLabel() {
    return firstNameLabel.get();
  }

  public StringProperty firstNameLabelProperty() {
    return firstNameLabel;
  }

  public void setFirstNameLabel(String firstNameLabel) {
    this.firstNameLabel.set(firstNameLabel);
  }

  public String getLastNameLabel() {
    return lastNameLabel.get();
  }

  public StringProperty lastNameLabelProperty() {
    return lastNameLabel;
  }

  public void setLastNameLabel(String lastNameLabel) {
    this.lastNameLabel.set(lastNameLabel);
  }

  public String getBloodPressureSystolicLabel() {
    return bloodPressureSystolicLabel.get();
  }

  public StringProperty bloodPressureSystolicLabelProperty() {
    return bloodPressureSystolicLabel;
  }

  public void setBloodPressureSystolicLabel(String bloodPressureSystolicLabel) {
    this.bloodPressureSystolicLabel.set(bloodPressureSystolicLabel);
  }

  public String getBloodPressureDiastolicLabel() {
    return bloodPressureDiastolicLabel.get();
  }

  public StringProperty bloodPressureDiastolicLabelProperty() {
    return bloodPressureDiastolicLabel;
  }

  public void setBloodPressureDiastolicLabel(String bloodPressureDiastolicLabel) {
    this.bloodPressureDiastolicLabel.set(bloodPressureDiastolicLabel);
  }

  public String getWeightLabel() {
    return weightLabel.get();
  }

  public StringProperty weightLabelProperty() {
    return weightLabel;
  }

  public void setWeightLabel(String weightLabel) {
    this.weightLabel.set(weightLabel);
  }

  public String getTallnessLabel() {
    return tallnessLabel.get();
  }

  public StringProperty tallnessLabelProperty() {
    return tallnessLabel;
  }

  public void setTallnessLabel(String tallnessLabel) {
    this.tallnessLabel.set(tallnessLabel);
  }

  public String getYearOfBirthLabel() {
    return yearOfBirthLabel.get();
  }

  public StringProperty yearOfBirthLabelProperty() {
    return yearOfBirthLabel;
  }

  public void setYearOfBirthLabel(String yearOfBirthLabel) {
    this.yearOfBirthLabel.set(yearOfBirthLabel);
  }

  public String getGenderLabel() {
    return genderLabel.get();
  }

  public StringProperty genderLabelProperty() {
    return genderLabel;
  }

  public void setGenderLabel(String genderLabel) {
    this.genderLabel.set(genderLabel);
  }

  public String getImageURLLabel() {
    return imageURLLabel.get();
  }

  public StringProperty imageURLLabelProperty() {
    return imageURLLabel;
  }

  public void setImageURLLabel(String imageURLLabel) {
    this.imageURLLabel.set(imageURLLabel);
  }

  public String getSelectPatientLabel() {
    return selectPatientLabel.get();
  }

  public StringProperty selectPatientLabelProperty() {
    return selectPatientLabel;
  }

  public void setSelectPatientLabel(String selectPatientLabel) {
    this.selectPatientLabel.set(selectPatientLabel);
  }

  public String getOkLabel() {
    return okLabel.get();
  }

  public StringProperty okLabelProperty() {
    return okLabel;
  }

  public void setOkLabel(String okLabel) {
    this.okLabel.set(okLabel);
  }

  public String getCancelLabel() {
    return cancelLabel.get();
  }

  public StringProperty cancelLabelProperty() {
    return cancelLabel;
  }

  public void setCancelLabel(String cancelLabel) {
    this.cancelLabel.set(cancelLabel);
  }
}
