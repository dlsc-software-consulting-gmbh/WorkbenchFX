package com.dlsc.workbenchfx.demo;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.demo.modules.maps.MapsModule;
import com.dlsc.workbenchfx.demo.modules.patient.PatientModule;
import com.dlsc.workbenchfx.demo.modules.preferences.Preferences;
import com.dlsc.workbenchfx.demo.modules.preferences.PreferencesModule;
import com.dlsc.workbenchfx.demo.modules.webview.WebModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class ExtendedDemo extends Application {

    private static final String DOCUMENTATION_PATH = WebModule.class.getResource("index.html").toExternalForm();

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

        CSSFX.start(myScene);

        initNightMode();
    }

    private Workbench initWorkbench() {
        // Navigation Drawer
        MenuItem item1 = new MenuItem("Complaints", new FontIcon(MaterialDesign.MDI_BOMB));
        MenuItem item2 = new MenuItem("Printing", new FontIcon(MaterialDesign.MDI_PRINTER));
        MenuItem item3 = new MenuItem("Settings", new FontIcon(MaterialDesign.MDI_SETTINGS));

        ToolbarItem showDialogButton = new ToolbarItem("Reset", new FontIcon(MaterialDesign.MDI_SETTINGS));

        // WorkbenchFX
        workbench = Workbench.builder(
                        new PatientModule(),
                        new CalendarModule(),
                        new MapsModule(),
                        new WebModule("JFX-Central", MaterialDesign.MDI_WEB, "https://jfx-central.com"),
                        new WebModule("Documentation", MaterialDesign.MDI_BOOK, DOCUMENTATION_PATH),
                        new WebModule("Notepad", MaterialDesign.MDI_NOTE, "https://docs.google.com"),
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
