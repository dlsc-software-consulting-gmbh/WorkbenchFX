package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarView extends HBox implements View {
    private final WorkbenchFx model;
    private FontAwesomeIconView homeIconView;
    Button homeBtn;
    private HBox tabBox;
    private HBox dropdownBox;

    // TODO: add javadoc comment
    public ToolBarView(WorkbenchFx model) {
        this.model = model;
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeSelf() {
        setId("toolbar");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {
        homeIconView = new FontAwesomeIconView(FontAwesomeIcon.HOME);
        homeIconView.setId("homeIconView");

        homeBtn = new Button("", homeIconView);
        homeBtn.setId("homeButton");
        homeBtn.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);

        tabBox = new HBox();
        tabBox.setId("tabBox");

        dropdownBox = new HBox();
        dropdownBox.setId("dropdownBox");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
        model.getDropdowns().forEach(dropdown -> dropdownBox.getChildren().add(dropdown));
        System.out.println(model.getDropdowns().size());
        getChildren().addAll(homeBtn, tabBox, dropdownBox);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindFieldsToModel() {

    }

    // TODO: add javadoc comment
    public void addTab(Node tab) {
        tabBox.getChildren().add(tab);
    }

    // TODO: add javadoc comment
    public void removeTab(int index) {
        tabBox.getChildren().remove(index);
    }

}
