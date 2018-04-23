package uk.co.senapt.desktop.shell;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import uk.co.senapt.desktop.shell.skins.HomeScreenSkin;

/**
 * Created by lemmi on 22.08.17.
 */
public class HomeScreen extends Control {

    private Shell shell;

    public HomeScreen(Shell shell) {
        this.shell = shell;
        getStyleClass().add("home-screen");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new HomeScreenSkin(this);
    }

    public Shell getShell() {
        return shell;
    }
}
