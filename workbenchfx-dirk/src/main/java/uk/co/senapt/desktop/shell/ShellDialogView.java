package uk.co.senapt.desktop.shell;

import javafx.scene.layout.VBox;

public class ShellDialogView extends VBox {

    public ShellDialogView(ShellDialog input) {
        setPrefSize(600, 600);
        setStyle("-fx-background-color: orange; -fx-opacity: .7;");
    }
}
