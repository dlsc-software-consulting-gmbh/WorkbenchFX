package uk.co.senapt.desktop.shell.util;

import com.google.common.base.Preconditions;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;

/**
 * Created by gdiaz on 12/09/2017.
 */
public final class SkinUtil {

    private SkinUtil() {
        super();
    }

    public static Label createIconLabel(StringProperty iconClassProperty) {
        Label iconLabel = new Label();
        if (iconClassProperty.get() != null) {
            iconLabel.getStyleClass().add(iconClassProperty.get());
        }
        iconClassProperty.addListener((obs, oldV, newV) -> {
            iconLabel.getStyleClass().remove(oldV);
            if (newV != null) {
                iconLabel.getStyleClass().add(newV);
            }
        });
        return iconLabel;
    }

    public static void runInFXThread(Runnable run) {
        if (Platform.isFxApplicationThread()) {
            run.run();
        } else {
            Platform.runLater(run);
        }
    }

    public static void initializeNodeFromFXML(Node node, Class<?> controlClass) {
        try {
            String fxmlResourceName = controlClass.getSimpleName() + ".fxml";
            FXMLLoader loader = new FXMLLoader(controlClass.getResource(fxmlResourceName));
            loader.setController(node);
            loader.setRoot(node);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkSkinControl(Skin<? extends Control> skin, Control control) {
        Preconditions.checkState(skin.getSkinnable() == control,
                "Skin has been attached to a different control." +
                        "  Expected='" + control + "', Actual='" + skin.getSkinnable() + "'");
    }

}
