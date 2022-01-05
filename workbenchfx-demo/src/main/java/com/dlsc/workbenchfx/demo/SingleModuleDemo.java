package com.dlsc.workbenchfx.demo;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.helloworld.HelloWorldModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

/**
 * Demonstrates the Workbench without the tab bar when only a single module is used.
 *
 * @author Steffen Schoen
 */
public class SingleModuleDemo extends Application {

    private HelloWorldModule helloWorldModule = new HelloWorldModule();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene myScene = new Scene(initWorkbench());

        CSSFX.start(myScene);

        primaryStage.setTitle("Single Module WorkbenchFX Demo");
        primaryStage.setScene(myScene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private Workbench initWorkbench() {
        ToolbarItem showDialogButton = new ToolbarItem("Reset", new FontIcon(MaterialDesign.MDI_SETTINGS));
        Workbench workbench = Workbench.builder(helloWorldModule).toolbarRight(showDialogButton).build();
        showDialogButton.setOnClick(event -> workbench.showConfirmationDialog("Reset settings", "Are you sure you want to reset all your settings?", null));
        return workbench;
    }
}
