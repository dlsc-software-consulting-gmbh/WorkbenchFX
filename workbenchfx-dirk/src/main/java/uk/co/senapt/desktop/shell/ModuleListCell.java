package uk.co.senapt.desktop.shell;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Created by gdiaz on 10/10/2017.
 */
public class ModuleListCell extends ListCell<ShellModule> {

    private static final String TOOLBAR_CLOSE_ICON = "toolbar-close-icon";

    private Region primaryColor = new Region();
    private Label moduleName = new Label();
    private Label closeIcon = new Label();
    private HBox container;

    public ModuleListCell(Shell shell, boolean showPrimaryColor) {
        getStyleClass().add("module-list-cell");
        primaryColor.getStyleClass().add("primary-color-inactive-field");

        if (showPrimaryColor) {
            container = new HBox(primaryColor, moduleName, closeIcon);
        } else {
            container = new HBox(moduleName, closeIcon);
        }

        closeIcon.getStyleClass().add(TOOLBAR_CLOSE_ICON);
        closeIcon.setOnMouseClicked(evt -> {
            final ShellModule module = getItem();
            if (module != null) {
                shell.closeModule(module);
            }
        });

        container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        container.getStyleClass().add("container");

        setGraphic(container);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        selectedProperty().addListener(it -> updatePrimaryColor());
    }

    private void updatePrimaryColor() {
        ShellModule module = getItem();
        if (module != null) {
            if (isSelected()) {
                if (!getStyleClass().contains(module.getPrimaryColorStyleClass())) {
                    getStyleClass().add(module.getPrimaryColorStyleClass());
                }
            } else {
                removePrimaryColorClasses();
            }

            primaryColor.getStyleClass().add(module.getPrimaryColorStyleClass());
        }
    }

    private boolean removePrimaryColorClasses() {
        return getStyleClass().removeIf(clazz -> clazz.startsWith("primary-color-inactive"));
    }

    @Override
    protected void updateItem(ShellModule module, boolean empty) {
        super.updateItem(module, empty);

        removePrimaryColorClasses();

        if (module != null && !empty) {
            moduleName.setText(module.getName());
            moduleName.getStyleClass().addAll(module.getTaskbarIconClass());
            updatePrimaryColor();
            container.setVisible(true);
        } else {
            container.setVisible(false);
        }
    }
}

