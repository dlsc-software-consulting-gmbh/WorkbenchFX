package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Callback
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class SelectionStripSpec extends ApplicationSpec {

    private SelectionStrip<WorkbenchModule> selectionStrip
    private FxRobot robot

    @Override
    void start(Stage stage) throws Exception {
        selectionStrip = new SelectionStrip<>()
        robot = new FxRobot()

        Scene scene = new Scene(selectionStrip, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "test if styleclass was set correctly"() {
        given: "String of styleclass which shall be set"
        String styleClass = "selection-strip"

        when: "the cell is created"
        selectionStrip = new SelectionStrip<>()

        then: "styleclass must be set"
        selectionStrip.getStyleClass().contains(styleClass)
    }

    def "test if maxWidth and Height were set correctly"() {
        given: "Double value of max and min which shall be set"
        double prefWidth = 400;
        double prefHeight = 50;

        when: "the cell is created"
        selectionStrip = new SelectionStrip<>()

        then: "max and min must be set"
        prefWidth == selectionStrip.getPrefWidth()
        prefHeight == selectionStrip.getPrefHeight()
    }

    def "set a cell factory"() {
        when: "initial setup"
        selectionStrip = new SelectionStrip<>()

        then: "cellfactory is instance of StripCell"
        selectionStrip.getCellFactory() instanceof Callback<SelectionStrip, StripCell<WorkbenchModule>>
    }

    /*
    Wurde styleklasse gesetzt? korrekt?
    prefwidth/height korrekt gesetzt?
    wurde cellfactory aufgerufen?
    wurde selectedItemProperty aufgerufen?

    wurde selectedItem aufgerufen?
    wurde isAutoscrolling aufgerufen?
    wurde scrollto aufgerufen?
    wurde getSelectedItem aufgerufen?
    requestLayout -> evtl.mock of parent?
     */
}
