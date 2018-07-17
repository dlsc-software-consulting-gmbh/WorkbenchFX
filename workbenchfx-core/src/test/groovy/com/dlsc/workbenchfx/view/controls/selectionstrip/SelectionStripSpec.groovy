package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Callback
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Shared

class SelectionStripSpec extends ApplicationSpec {

    private SelectionStrip<WorkbenchModule> selectionStrip
    private FxRobot robot

    @Shared
    private WorkbenchModule workbenchModule = Mock()
    @Shared
    private WorkbenchModule workbenchModule2 = Mock()

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

    def "tests behaviour of adding a module and setting it selected"(WorkbenchModule module, WorkbenchModule selectedModule, int listSize) {
        given:
        robot.interact {
            selectionStrip.getItems().add(module)
        }

        expect:
        robot.interact {
            selectedModule == selectionStrip.getSelectedItem()
            listSize == selectionStrip.getItems().size()
        }

        where:
        module          || selectedModule  | listSize
        null            || null            | 0
        workbenchModule || workbenchModule | 1
    }

    @Shared
    SelectionStrip<WorkbenchModule> selectionStripShared

    def "tests behaviour of adding multiple modules and setting them selected"(WorkbenchModule module, WorkbenchModule selectedModule, int listSize) {
        given:
        robot.interact {
            selectionStripShared = new SelectionStrip<>()
            selectionStripShared.getItems().add(module)
        }

        expect:
        robot.interact {
            selectedModule == selectionStrip.getSelectedItem()
            listSize == selectionStrip.getItems().size()
        }

        where:
        module           || selectedModule   | listSize
        null             || null             | 0
        workbenchModule  || workbenchModule  | 1
        workbenchModule2 || workbenchModule2 | 2
        workbenchModule2 || workbenchModule2 | 3
    }

    /*
    wurde selectedItem aufgerufen?
    wurde isAutoscrolling aufgerufen?
    wurde scrollto aufgerufen?
    wurde getSelectedItem aufgerufen?
    requestLayout -> evtl.mock of parent?
     */
}
