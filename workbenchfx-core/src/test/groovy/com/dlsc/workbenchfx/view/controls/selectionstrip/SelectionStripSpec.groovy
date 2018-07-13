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

    def "tests if setting an item sets them active the correct way"() {
        given: "WorkbenchModule-mocks as items"
        WorkbenchModule workbenchModule = Mock()
        WorkbenchModule workbenchModule2 = Mock()
        int size = selectionStrip.getItems().size()

        when: "adding null as parameter"
        robot.interact {
            selectionStrip.getItems().add(null)
        }

        then: "the active module is null and the size of the list is 0"
        robot.interact {
            Objects.isNull(selectionStrip.getSelectedItem())
            size == selectionStrip.getItems().size()
        }

        when: "adding a module"
        robot.interact {
            selectionStrip.getItems().add(workbenchModule)
        }

        then: "the active module is the one hand over"
        robot.interact {
            workbenchModule == selectionStrip.getSelectedItem()
            size == selectionStrip.getItems().size()
        }

        when: "setting another Module"
        robot.interact {
            selectionStrip.getItems().add(workbenchModule2)
        }

        then: "the active module should change to the second one"
        robot.interact {
            workbenchModule == selectionStrip.getSelectedItem()
            size + 1 == selectionStrip.getItems().size()
        }

        when: "setting the second module again"
        robot.interact {
            selectionStrip.getItems().add(workbenchModule2)
        }

        then: "it will be added and the active module should change to the second one"
        robot.interact {
            workbenchModule2 == selectionStrip.getSelectedItem()
            size + 2 == selectionStrip.getItems().size()
        }
    }

    def "bla"() {
        given: ""
        WorkbenchModule workbenchModule = Mock()
        WorkbenchModule workbenchModule2 = Mock()

        when: ""
        boolean areSame = workbenchModule.equals(workbenchModule2)

        then: ""
        1 * workbenchModule.equals(_) >> true
        areSame
    }

    def "bla2"() {
        given: ""
        SelectionStrip<WorkbenchModule> selectionStrip = Mock()
        WorkbenchModule workbenchModule = Mock()

        when: ""
        WorkbenchModule module = selectionStrip.getSelectedItem()

        then: ""
        1 * selectionStrip.getSelectedItem() >> workbenchModule
        workbenchModule == module
    }

    /*
    wurde selectedItem aufgerufen?
    wurde isAutoscrolling aufgerufen?
    wurde scrollto aufgerufen?
    wurde getSelectedItem aufgerufen?
    requestLayout -> evtl.mock of parent?
     */
}
