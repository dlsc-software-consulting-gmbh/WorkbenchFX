package com.dlsc.workbenchfx.modules.patient.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dieter Holz
 */
public class Patient {

  public enum Gender {
    MALE, FEMALE, X
  }

  private LongProperty id = new SimpleLongProperty();
  private StringProperty firstName = new SimpleStringProperty();
  private StringProperty lastName = new SimpleStringProperty();
  private DoubleProperty bloodPressureSystolic = new SimpleDoubleProperty();
  private DoubleProperty bloodPressureDiastolic = new SimpleDoubleProperty();
  private DoubleProperty weight = new SimpleDoubleProperty();
  private DoubleProperty tallness = new SimpleDoubleProperty();
  private IntegerProperty yearOfBirth = new SimpleIntegerProperty();
  private ObjectProperty<Gender> gender = new SimpleObjectProperty<>();
  private StringProperty imageURL = new SimpleStringProperty();


  public Patient() {
    setId(System.nanoTime());
  }

  public Patient(String[] args) {
    setId(Long.valueOf(args[0]));
    setFirstName(args[1]);
    setLastName(args[2]);
    setBloodPressureSystolic(Double.valueOf(args[3]));
    setBloodPressureDiastolic(Double.valueOf(args[4]));
    setWeight(Double.valueOf(args[5]));
    setTallness(Double.valueOf(args[6]));
    setYearOfBirth(Integer.valueOf(args[7]));
    setGender(Gender.valueOf(args[8]));
    setImageURL(args[9]);
  }

  public String toExternalString(String delimiter) {
    return String.join(delimiter,
        Long.toString(getId()),
        getFirstName(),
        getLastName(),
        Double.toString(getBloodPressureSystolic()),
        Double.toString(getBloodPressureDiastolic()),
        Double.toString(getWeight()),
        Double.toString(getTallness()),
        Integer.toString(getYearOfBirth()),
        getGender().name(),
        getImageURL());
  }
  // setter and getter

  public long getId() {
    return id.get();
  }

  public LongProperty idProperty() {
    return id;
  }

  public void setId(long id) {
    this.id.set(id);
  }

  public String getFirstName() {
    return firstName.get();
  }

  public StringProperty firstNameProperty() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName.set(firstName);
  }

  public String getLastName() {
    return lastName.get();
  }

  public StringProperty lastNameProperty() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName.set(lastName);
  }

  public String getImageURL() {
    return imageURL.get();
  }

  public StringProperty imageURLProperty() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL.set(imageURL);
  }

  public double getBloodPressureDiastolic() {
    return bloodPressureDiastolic.get();
  }

  public DoubleProperty bloodPressureDiastolicProperty() {
    return bloodPressureDiastolic;
  }

  public void setBloodPressureDiastolic(double bloodPressureDiastolic) {
    this.bloodPressureDiastolic.set(bloodPressureDiastolic);
  }

  public double getBloodPressureSystolic() {
    return bloodPressureSystolic.get();
  }

  public DoubleProperty bloodPressureSystolicProperty() {
    return bloodPressureSystolic;
  }

  public void setBloodPressureSystolic(double bloodPressureSystolic) {
    this.bloodPressureSystolic.set(bloodPressureSystolic);
  }

  public double getWeight() {
    return weight.get();
  }

  public DoubleProperty weightProperty() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight.set(weight);
  }

  public double getTallness() {
    return tallness.get();
  }

  public DoubleProperty tallnessProperty() {
    return tallness;
  }

  public void setTallness(double tallness) {
    this.tallness.set(tallness);
  }

  public int getYearOfBirth() {
    return yearOfBirth.get();
  }

  public IntegerProperty yearOfBirthProperty() {
    return yearOfBirth;
  }

  public void setYearOfBirth(int yearOfBirth) {
    this.yearOfBirth.set(yearOfBirth);
  }

  public Gender getGender() {
    return gender.get();
  }

  public ObjectProperty<Gender> genderProperty() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender.set(gender);
  }
}
