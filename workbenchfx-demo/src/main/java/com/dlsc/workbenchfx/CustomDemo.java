package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.controls.CustomNavigationDrawer;
import com.dlsc.workbenchfx.controls.CustomOverlay;
import com.dlsc.workbenchfx.controls.CustomPage;
import com.dlsc.workbenchfx.controls.CustomTab;
import com.dlsc.workbenchfx.controls.CustomTile;
import com.dlsc.workbenchfx.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.modules.gantt.GanttModule;
import com.dlsc.workbenchfx.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.modules.maps.MapsModule;
import com.dlsc.workbenchfx.modules.patient.PatientModule;
import com.dlsc.workbenchfx.modules.preferences.Preferences;
import com.dlsc.workbenchfx.modules.preferences.PreferencesModule;
import com.dlsc.workbenchfx.modules.test.DialogTestModule;
import com.dlsc.workbenchfx.modules.test.DrawerTestModule;
import com.dlsc.workbenchfx.modules.test.InterruptClosing2TestModule;
import com.dlsc.workbenchfx.modules.test.InterruptClosingTestModule;
import com.dlsc.workbenchfx.modules.test.LifecycleTestModule;
import com.dlsc.workbenchfx.modules.test.NavigationDrawerTestModule;
import com.dlsc.workbenchfx.modules.test.ToolbarItemTestModule;
import com.dlsc.workbenchfx.modules.test.ToolbarTestModule;
import com.dlsc.workbenchfx.modules.test.WidgetsTestModule;
import com.dlsc.workbenchfx.modules.webview.WebModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomDemo extends Application {

  private static final Logger LOGGER = LogManager.getLogger(CustomDemo.class.getName());
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
    Menu menu1 = new Menu("Customer", createIcon(MaterialDesignIcon.ACCOUNT_MULTIPLE));
    Menu menu2 = new Menu("Tariff Management", createIcon(MaterialDesignIcon.DOMAIN));
    Menu menu3 = new Menu("Complaints", createIcon(MaterialDesignIcon.BOMB));

    MaterialDesignIcon genericIcon = MaterialDesignIcon.HELP;
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

    MenuItem itemA = new MenuItem("Complaints", createIcon(MaterialDesignIcon.BOMB));
    MenuItem itemB = new MenuItem("Printing", createIcon(MaterialDesignIcon.PRINTER));
    MenuItem itemC = new MenuItem("Settings", createIcon(MaterialDesignIcon.SETTINGS));

    MenuItem showOverlay = new MenuItem("Show overlay");
    MenuItem showBlockingOverlay = new MenuItem("Show blocking overlay");

    item21.getItems().addAll(item211, item212, item213, item214, item215);

    menu1.getItems().addAll(item11, item12, item13, item14);
    menu2.getItems().addAll(item21, item22);
    menu3.getItems().addAll(item31, item32, item33);

    ToolbarItem addPreferences = new ToolbarItem("Add",
        new FontAwesomeIconView(FontAwesomeIcon.GEARS));
    ToolbarItem removePreferences = new ToolbarItem("Remove",
        new FontAwesomeIconView(FontAwesomeIcon.GEARS));
    ToolbarItem showDialogButton = new ToolbarItem("Show",
        new FontAwesomeIconView(FontAwesomeIcon.GEARS));

    // WorkbenchFX
    workbench =
        Workbench.builder(
            new PatientModule(),
            new CalendarModule(),
            new GanttModule(),
            new MapsModule(),
            new WebModule("DLSC", MaterialDesignIcon.WEB, "http://dlsc.com"),
            new WebModule("Documentation", MaterialDesignIcon.BOOK, DOCUMENTATION_PATH),
            new WebModule("Notepad", MaterialDesignIcon.NOTE, "https://docs.google.com"),
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
                        new FontAwesomeIconView(FontAwesomeIcon.PLUS),
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

    workbench
        .getStylesheets()
        .add(CustomDemo.class.getResource("customOverlay.css").toExternalForm());

    return workbench;
  }

  private Node createIcon(MaterialDesignIcon icon) {
    MaterialDesignIconView materialDesignIconView = new MaterialDesignIconView(icon);
    materialDesignIconView.getStyleClass().add("icon");
    return materialDesignIconView;
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
