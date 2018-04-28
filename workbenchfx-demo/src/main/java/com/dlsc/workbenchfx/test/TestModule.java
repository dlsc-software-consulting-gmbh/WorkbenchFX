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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class TestModule extends AbstractModule {
    private int index = 1;

    private final Button addMenuBtn = new Button("Add new Dropdown");
    private final Button addItemBtn = new Button("Add MenuItem to Dropdown");
    private final Dropdown customDropdown = Dropdown.of(
            "New MenuButton",
            new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION)
    );

    private final HBox customPane = new HBox();

    public TestModule() {
        super("Test Module", FontAwesomeIcon.QUESTION);
        layoutParts();
        setupEventHandlers();
    }

    private void layoutParts() {
        customPane.getChildren().addAll(addMenuBtn, addItemBtn);
        customPane.setAlignment(Pos.CENTER);
    }

    private void setupEventHandlers() {
        addMenuBtn.setOnAction(event -> {
            workbench.addToolBarControl(customDropdown);
            addMenuBtn.setDisable(true);
        });

        addItemBtn.setOnAction(
                event -> customDropdown.getItems().add(new CustomMenuItem(new Label("New Item " + index++)))
        );
    }

    @Override
    public Node activate() {
        return customPane;
    }
}
