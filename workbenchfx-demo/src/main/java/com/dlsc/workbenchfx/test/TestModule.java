package com.dlsc.workbenchfx.test;

import com.dlsc.workbenchfx.module.AbstractModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TestModule extends AbstractModule {
    private int index = 1;

    private final Button addMenuBtn = new Button("Add new Dropdown");
    private final Button addItemBtn = new Button("Add MenuItem to Dropdown");
    private final Button removeMenuBtn = new Button("Remove new Dropdown");
    private final Button removeItemBtn = new Button("Remove MenuItem from Dropdown");
    private final Dropdown customDropdown = Dropdown.of(
            "New MenuButton",
            new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION)
    );
    private final List<MenuItem> itemsLst = new ArrayList<>();

    private final GridPane customPane = new GridPane();

    public TestModule() {
        super("Test Module", FontAwesomeIcon.QUESTION);
        layoutParts();
        setupEventHandlers();
    }

    private void layoutParts() {
        customPane.add(addMenuBtn, 0, 0);
        customPane.add(addItemBtn, 0, 1);
        customPane.add(removeItemBtn, 1, 1);
        customPane.add(removeMenuBtn, 1, 0);
        customPane.setAlignment(Pos.CENTER);
    }

    private void setupEventHandlers() {
        addMenuBtn.setOnAction(event -> {
            workbench.addToolBarControl(customDropdown);
        });

        addItemBtn.setOnAction(
                event -> {
                    itemsLst.add(new CustomMenuItem(new Label("New Item " + index++)));
                    customDropdown.getItems().add(itemsLst.get(itemsLst.size() - 1));
                }
        );

        removeMenuBtn.setOnAction(event -> {
            workbench.removeToolBarControl(customDropdown);
        });

        removeItemBtn.setOnAction(
                event -> {
                    if (itemsLst.size() == 0) {
                        customDropdown.getItems().remove(itemsLst.remove(itemsLst.size() - 1));
                    }
                }
        );
    }

    @Override
    public Node activate() {
        return customPane;
    }
}
