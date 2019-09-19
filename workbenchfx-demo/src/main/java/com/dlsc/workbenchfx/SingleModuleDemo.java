package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.modules.gantt.GanttModule;
import com.dlsc.workbenchfx.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Demonstrates the Workbench without the tab bar when only a single module is used
 * @author Steffen Schoen
 */
public class SingleModuleDemo extends Application {

    private Workbench workbench;
    private HelloWorldModule helloWorldModule = new HelloWorldModule();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene myScene = new Scene(initWorkbench());

        primaryStage.setTitle("Single Module WorkbenchFX Demo");
        primaryStage.setScene(myScene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private Workbench initWorkbench() {
        ToolbarItem showDialogButton = new ToolbarItem("Reset",
                new MaterialDesignIconView(MaterialDesignIcon.SETTINGS));

        showDialogButton.setOnClick(event -> workbench.showConfirmationDialog("Reset settings",
                "Are you sure you want to reset all your settings?", null));

        workbench = Workbench.builder(
                helloWorldModule
        )   .toolbarRight(showDialogButton)
            .build();
        return workbench;
    }
}
