/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.senapt.desktop;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
//import uk.co.senapt.desktop.shell.ModulesManager;
import uk.co.senapt.desktop.shell.Shell;
import uk.co.senapt.desktop.shell.model.Favorite;

/**
 * @author user
 */
public class ShellApp extends Application {

    private static final int MAX_WINDOW_WIDTH = 1600;
    private static final int MAX_WINDOW_HEIGHT = 1200;

    @Override
    public void start(Stage primaryStage) {
        //final ModulesManager modulesManager = new ModulesManager();

        Shell shell = new Shell();
        //shell.getModules().addAll(modulesManager.getModules());

        Favorite fav1 = new Favorite("Favorite 1", "module-small");
        Favorite fav2 = new Favorite("Favorite 2", "module-small");

        shell.getFavorites().addAll(fav1, fav2);


        Scene scene = new Scene(shell);

        scene.getStylesheets().add(ShellApp.class.getResource("ui/desktop.css").toExternalForm());
        scene.getStylesheets().add(ShellApp.class.getResource("ui/desktop-icons.css").toExternalForm());

        /*
         * Add the stylesheets required by the modules to the scene.
         */
        /*modulesManager.getModules().forEach(module -> {
            scene.getStylesheets().addAll(module.getModuleStylesheets());
        });*/

        primaryStage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        final double windowWidth = Math.max(1400, Math.min(MAX_WINDOW_WIDTH, primaryScreenBounds.getWidth() * .7));
        final double windowHeight = Math.max(900, Math.min(MAX_WINDOW_HEIGHT, primaryScreenBounds.getHeight() * .7));

        primaryStage.setTitle("Senapt CRM");
        primaryStage.setWidth(windowWidth);
        primaryStage.setHeight(windowHeight);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
