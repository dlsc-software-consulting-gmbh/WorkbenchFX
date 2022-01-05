package com.dlsc.workbenchfx.demo;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.controls.*;
import com.dlsc.workbenchfx.demo.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.demo.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.demo.modules.maps.MapsModule;
import com.dlsc.workbenchfx.demo.modules.patient.PatientModule;
import com.dlsc.workbenchfx.demo.modules.preferences.Preferences;
import com.dlsc.workbenchfx.demo.modules.preferences.PreferencesModule;
import com.dlsc.workbenchfx.demo.modules.test.*;
import com.dlsc.workbenchfx.demo.modules.webview.WebModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class CustomDemo extends Application {

    private static final String DOCUMENTATION_PATH =
            WebModule.class.getResource("index.html").toExternalForm();

    private Workbench workbench;
    private Preferences preferences;

    private PreferencesModule preferencesModule;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        preferences = new Preferences();
        preferencesModule = new PreferencesModule(preferences);

        Scene myScene = new Scene(initWorkbench());

        CSSFX.start(myScene);

        primaryStage.setTitle("Custom WorkbenchFX Demo");
        primaryStage.setScene(myScene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.show();
        primaryStage.centerOnScreen();

        initNightMode();
    }

    private Workbench initWorkbench() {
        // Navigation Drawer
        Menu menu1 = new Menu("Customer", createIcon(MaterialDesign.MDI_ACCOUNT_MULTIPLE));
        Menu menu2 = new Menu("Tariff Management", createIcon(MaterialDesign.MDI_DOMAIN));
        Menu menu3 = new Menu("Complaints", createIcon(MaterialDesign.MDI_BOMB));

        Ikon genericIcon = MaterialDesign.MDI_HELP;
        MenuItem item11 = new MenuItem("Item 1.1", createIcon(genericIcon));
        MenuItem item12 = new MenuItem("Item 1.2", createIcon(genericIcon));
        MenuItem item13 = new MenuItem("Item 1.3", createIcon(genericIcon));
        MenuItem item14 = new MenuItem("Item 1.4", createIcon(genericIcon));

        Menu item21 = new Menu("Item 2.1", createIcon(genericIcon));
        MenuItem item22 = new MenuItem("Item 2.2", createIcon(genericIcon));

        MenuItem item211 = new MenuItem("Item 2.1.1", createIcon(genericIcon));
        MenuItem item212 = new MenuItem("Item 2.1.2", createIcon(genericIcon));
        MenuItem item213 = new MenuItem("Item 2.1.3", createIcon(genericIcon));
        MenuItem item214 = new MenuItem("Item 2.1.4", createIcon(genericIcon));
        MenuItem item215 = new MenuItem("Item 2.1.5", createIcon(genericIcon));

        MenuItem item31 = new MenuItem("Item 3.1", createIcon(genericIcon));
        MenuItem item32 = new MenuItem("Item 3.2", createIcon(genericIcon));
        MenuItem item33 = new MenuItem("Item 3.3", createIcon(genericIcon));

        MenuItem itemA = new MenuItem("Complaints", createIcon(MaterialDesign.MDI_BOMB));
        MenuItem itemB = new MenuItem("Printing", createIcon(MaterialDesign.MDI_PRINTER));
        MenuItem itemC = new MenuItem("Settings", createIcon(MaterialDesign.MDI_SETTINGS));

        MenuItem showOverlay = new MenuItem("Show overlay");
        MenuItem showBlockingOverlay = new MenuItem("Show blocking overlay");

        item21.getItems().addAll(item211, item212, item213, item214, item215);

        menu1.getItems().addAll(item11, item12, item13, item14);
        menu2.getItems().addAll(item21, item22);
        menu3.getItems().addAll(item31, item32, item33);

        ToolbarItem addPreferences = new ToolbarItem("Add", new FontIcon(FontAwesome.GEARS));
        ToolbarItem removePreferences = new ToolbarItem("Remove", new FontIcon(FontAwesome.GEARS));
        ToolbarItem showDialogButton = new ToolbarItem("Show", new FontIcon(FontAwesome.GEARS));

        // WorkbenchFX
        workbench = Workbench.builder(
                        new PatientModule(),
                        new CalendarModule(),
                        new MapsModule(),
                        new WebModule("JFX-Central", MaterialDesign.MDI_WEB, "https://jfx-central.com"),
                        new WebModule("Documentation", MaterialDesign.MDI_BOOK, DOCUMENTATION_PATH),
                        new WebModule("Notepad", MaterialDesign.MDI_NOTE, "https://docs.google.com"),
                        preferencesModule,
                        new HelloWorldModule(),
                        new ToolbarTestModule(),
                        new WidgetsTestModule(),
                        new ToolbarItemTestModule(),
                        new NavigationDrawerTestModule(),
                        new InterruptClosingTestModule(),
                        new InterruptClosing2TestModule(),
                        new DialogTestModule(),
                        new DrawerTestModule(),
                        new LifecycleTestModule()
                )
                .toolbarLeft(
                        new ToolbarItem("WorkbenchFX"),
                        addPreferences,
                        removePreferences
                )
                .toolbarRight(
                        showDialogButton,
                        new ToolbarItem(
                                new ImageView(CustomDemo.class.getResource("user.png").toExternalForm()),
                                new Menu(
                                        "Submenus",
                                        new FontIcon(FontAwesome.PLUS),
                                        new MenuItem("Submenu 1"),
                                        new CustomMenuItem(new Label("CustomMenuItem"), false))),
                        new ToolbarItem(
                                "Text",
                                new ImageView(CustomDemo.class.getResource("user.png").toExternalForm()),
                                new CustomMenuItem(new Label("Content 1")),
                                new CustomMenuItem(new Label("Content 2"))))
                .modulesPerPage(9)
                .pageFactory(CustomPage::new)
                .tabFactory(CustomTab::new)
                .tileFactory(CustomTile::new)
                .navigationDrawer(new CustomNavigationDrawer())
                .navigationDrawerItems(
                        menu1, menu2, menu3, itemA, itemB, itemC, showOverlay, showBlockingOverlay)
                .build();

        CustomOverlay customOverlay = new CustomOverlay(workbench, false);
        CustomOverlay blockingCustomOverlay = new CustomOverlay(workbench, true);
        showOverlay.setOnAction(event -> workbench.showOverlay(customOverlay, false));
        showBlockingOverlay.setOnAction(event -> workbench.showOverlay(blockingCustomOverlay, true));
        addPreferences.setOnClick(event -> workbench.getModules().add(preferencesModule));
        removePreferences.setOnClick(event -> workbench.getModules().remove(preferencesModule));
        showDialogButton.setOnClick(event -> workbench.showConfirmationDialog("Reset settings?",
                "This will reset your device to its default factory settings.", null));

        // This sets the custom style. Comment this out to have a look at the default styles.
        workbench.getStylesheets().add(CustomDemo.class.getResource("customTheme.css").toExternalForm());
        //workbench.getStylesheets().add(CustomDemo.class.getResource("darkTheme.css").toExternalForm());

        workbench.getStylesheets().add(CustomDemo.class.getResource("customOverlay.css").toExternalForm());

        return workbench;
    }

    private Node createIcon(Ikon icon) {
        return new FontIcon(icon);
    }

    private void initNightMode() {
        // initially set stylesheet
        setNightMode(preferences.isNightMode());

        // change stylesheet depending on whether nightmode is on or not
        preferences.nightModeProperty().addListener((observable, oldValue, newValue) -> setNightMode(newValue));
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
