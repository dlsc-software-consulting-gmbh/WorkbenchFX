package com.dlsc.workbenchfx.modules.patient.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Dieter Holz
 */
public class FileCabinet {
  private static final String DATA = "/com/dlsc/workbenchfx/modules/patient/data/PATIENT.csv";
  private static final String DELIMITER = ";";

  private final ObservableList<Patient> allPatients = FXCollections.observableArrayList();

  private ObjectProperty<Patient> selectedPatient = new SimpleObjectProperty<>();

  public FileCabinet() {
    allPatients.setAll(readFromFile());
  }

  public ObservableList<Patient> getAllPatients() {
    return allPatients;
  }

  private List<Patient> readFromFile() {
    try (Stream<String> stream = getStreamOfLines()) {
      return stream.skip(1)
          .map(line -> new Patient(line.split(DELIMITER, 10)))
          .collect(Collectors.toList());
    }
  }


  public void save() {
    try (BufferedWriter writer = Files.newBufferedWriter(getTempFile().toPath())) {
      writer.write(
          "id;firstName;lastName;bloodPressureSystolic;bloodPressureDiastolic;weight;tallness;age;gender;imageURL");
      writer.newLine();
      allPatients.stream()
          .map(result -> result.toExternalString(DELIMITER))
          .forEach(line -> {
            try {
              writer.write(line);
              writer.newLine();
            } catch (IOException e) {
              throw new IllegalStateException(e);
            }
          });
    } catch (IOException e) {
      throw new IllegalStateException("save failed");
    }
  }

  private Stream<String> getStreamOfLines() {
    copyDataToTempDirIfNecessary();
    try {
      return Files.lines(getTempFile().toPath(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("tmp file not found");
    }
  }

  private void copyDataToTempDirIfNecessary() {
    File tmpFile = getTempFile();
    if (tmpFile.exists()) {
      return;
    }

    tmpFile.getParentFile().mkdirs();

    try {
      InputStream resource = getClass().getResourceAsStream(DATA);
      if (resource != null) {
        Files.copy(resource, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      } else {
        tmpFile.createNewFile();
      }
    } catch (IOException e) {
      throw new IllegalStateException("copy file to tmpdir failed");
    }
  }

  private File getTempFile() {
    return new File(System.getProperty("java.io.tmpdir"), DATA);
  }

  public Patient getSelectedPatient() {
    return selectedPatient.get();
  }

  public ObjectProperty<Patient> selectedPatientProperty() {
    return selectedPatient;
  }

  public void setSelectedPatient(Patient selectedPatient) {
    this.selectedPatient.set(selectedPatient);
  }
}
