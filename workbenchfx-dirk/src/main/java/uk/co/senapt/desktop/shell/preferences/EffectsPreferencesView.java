package uk.co.senapt.desktop.shell.preferences;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import uk.co.senapt.desktop.shell.Shell;

public class EffectsPreferencesView extends VBox {

    @Inject
    public EffectsPreferencesView(Shell shell) {
        getStyleClass().add("effects");

        addBox("Animate menus when showing / hiding.", shell.animateMenusProperty());
        addBox("Animate dialogs when showing / hiding.", shell.animateDialogsProperty());
        addBox("Animate trays when showing / hiding.", shell.animateTraysProperty());
        addBox("Use fade in / fade out when showing / hiding elements.", shell.fadeInOutProperty());
        addBox("Show drop shadows in scrolling areas.", shell.showShadowProperty());
    }

    private void addBox(String text, BooleanProperty observable) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.selectedProperty().bindBidirectional(observable);
        getChildren().add(checkBox);
    }
}
