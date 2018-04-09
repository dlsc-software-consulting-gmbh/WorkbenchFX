package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import java.util.Arrays;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

public class RootPane extends StackPane {

  public WorkbenchFx workbenchFx;

  // Property for demo
//  DoubleProperty coolnessOfPreferencesFx = new SimpleDoubleProperty(10.0);

  // General
//  StringProperty welcomeText = new SimpleStringProperty("Hello World");
//  IntegerProperty brightness = new SimpleIntegerProperty(50);
//  BooleanProperty nightMode = new SimpleBooleanProperty(true);

  // Screen
//  DoubleProperty scaling = new SimpleDoubleProperty(1);
//  StringProperty screenName = new SimpleStringProperty("WorkbenchFx Monitor");

//  ObservableList<String> resolutionItems = FXCollections.observableArrayList(Arrays.asList(
//      "1024x768", "1280x1024", "1440x900", "1920x1080")
//  );
//  ObjectProperty<String> resolutionSelection = new SimpleObjectProperty<>("1024x768");

//  ListProperty<String> orientationItems = new SimpleListProperty<>(
//      FXCollections.observableArrayList(Arrays.asList("Vertical", "Horizontal"))
//  );
//  ObjectProperty<String> orientationSelection = new SimpleObjectProperty<>("Vertical");

//  IntegerProperty fontSize = new SimpleIntegerProperty(12);
//  DoubleProperty lineSpacing = new SimpleDoubleProperty(1.5);

  // Favorites
//  ListProperty<String> favoritesItems = new SimpleListProperty<>(
//      FXCollections.observableArrayList(Arrays.asList(
//          "eMovie", "Eboda Phot-O-Shop", "Mikesoft Text",
//          "Mikesoft Numbers", "Mikesoft Present", "IntelliG"
//          )
//      )
//  );
//  ListProperty<String> favoritesSelection = new SimpleListProperty<>(
//      FXCollections.observableArrayList(Arrays.asList(
//          "Eboda Phot-O-Shop", "Mikesoft Text"))
//  );

  // Custom Control
//  IntegerProperty customControlProperty = new SimpleIntegerProperty(42);
  //IntegerField customControl = setupCustomControl();

  public RootPane() {
    workbenchFx = WorkbenchFx.of();
    getChildren().add(new DemoView(workbenchFx));
  }

//  private WorkbenchFx createPreferences() {
//    // asciidoctor Documentation - tag::setupPreferences[]
//    return null;
//  }
}
