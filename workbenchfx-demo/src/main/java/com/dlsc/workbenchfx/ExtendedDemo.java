package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.calendar.CalendarModule;
import com.dlsc.workbenchfx.modules.customer.CustomerModule;
import com.dlsc.workbenchfx.modules.gantt.GanttModule;
import com.dlsc.workbenchfx.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.modules.maps.MapsModule;
import com.dlsc.workbenchfx.modules.patient.PatientModule;
import com.dlsc.workbenchfx.modules.webview.WebModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ExtendedDemo extends Application {

  private static final String DOCUMENTATION_PATH =
      WebModule.class.getResource("index.html").toExternalForm();

  public Workbench workbench;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myScene = new Scene(initWorkbench());

    primaryStage.setTitle("Extended WorkbenchFX Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
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
            new HelloWorldModule(),
            new WebModule("DLSC",  MaterialDesignIcon.WEB,"http://dlsc.com"),
            new WebModule("Notepad", MaterialDesignIcon.NOTE, "https://docs.google.com"),
            new WebModule("Documentation", MaterialDesignIcon.BOOK, DOCUMENTATION_PATH),
            new GanttModule(),
            new MapsModule()
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
}
