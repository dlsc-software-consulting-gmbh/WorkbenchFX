package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.modules.gantt.GanttModule;
import com.dlsc.workbenchfx.modules.maps.MapsModule;
import com.dlsc.workbenchfx.modules.patient.PatientModule;
import com.dlsc.workbenchfx.modules.preferences.Preferences;
import com.dlsc.workbenchfx.modules.preferences.PreferencesModule;
import com.dlsc.workbenchfx.modules.webview.WebModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ExtendedDemo extends Application {

  private static final String DOCUMENTATION_PATH =
      WebModule.class.getResource("index.html").toExternalForm();

  private Workbench workbench;
  private Preferences preferences;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    preferences = new Preferences();

    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("Extended WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();

    initNightMode();
  }

  private Workbench initWorkbench() {
    // Navigation Drawer
    MenuItem item1 = new MenuItem("Complaints", new MaterialDesignIconView(MaterialDesignIcon.BOMB));
    MenuItem item2 = new MenuItem("Printing", new MaterialDesignIconView(MaterialDesignIcon.PRINTER));
    MenuItem item3 = new MenuItem("Settings", new MaterialDesignIconView(MaterialDesignIcon.SETTINGS));

    ToolbarItem showDialogButton = new ToolbarItem("Reset",
        new MaterialDesignIconView(MaterialDesignIcon.SETTINGS));

    // WorkbenchFX
    workbench =
        Workbench.builder(
            new PatientModule(),
            new CalendarModule(),
            new GanttModule(),
            new MapsModule(),
            new WebModule("DLSC",  MaterialDesignIcon.WEB,"http://dlsc.com"),
            new WebModule("Documentation", MaterialDesignIcon.BOOK, DOCUMENTATION_PATH),
            new WebModule("Notepad", MaterialDesignIcon.NOTE, "https://docs.google.com"),
            new PreferencesModule(preferences)
        )
            .toolbarLeft(new ToolbarItem("WorkbenchFX"))
            .toolbarRight(showDialogButton)
            .navigationDrawerItems(item1, item2, item3)
            .build();

    showDialogButton.setOnClick(event -> workbench.showConfirmationDialog("Reset settings",
        "Are you sure you want to reset all your settings?", null));

    item1.setOnAction(event -> workbench.hideNavigationDrawer());
    item2.setOnAction(event -> workbench.hideNavigationDrawer());
    item3.setOnAction(event -> workbench.hideNavigationDrawer());

    // This sets the custom style. Comment this out to have a look at the default styles.
    workbench.getStylesheets().add(ExtendedDemo.class.getResource("customTheme.css").toExternalForm());
    //workbench.getStylesheets().add(ExtendedDemo.class.getResource("darkTheme.css").toExternalForm());

    return workbench;
  }

  private void initNightMode() {
    // initially set stylesheet
    setNightMode(preferences.isNightMode());

    // change stylesheet depending on whether nightmode is on or not
    preferences.nightModeProperty().addListener(
        (observable, oldValue, newValue) -> setNightMode(newValue)
    );
  }

  private void setNightMode(boolean on) {
    String customTheme = CustomDemo.class.getResource("customTheme.css").toExternalForm();
    String darkTheme = CustomDemo.class.getResource("darkTheme.css").toExternalForm();
    ObservableList<String> stylesheets = workbench.getStylesheets();
    if (on) {
      stylesheets.remove(customTheme);
      stylesheets.add(darkTheme);
    } else {
      stylesheets.remove(darkTheme);
      stylesheets.add(customTheme);
    }
  }
}
