package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.testing.MockSelectionStrip
import javafx.beans.WeakInvalidationListener
import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class StripCellSpec extends ApplicationSpec {

    private StripCell<WorkbenchModule> stripCell
    private SelectionStrip<WorkbenchModule> mockSelectionStrip
    private FxRobot robot

    @Override
    void start(Stage stage) throws Exception {
        stripCell = new StripCell<>()
        mockSelectionStrip = new MockSelectionStrip()

        robot = new FxRobot()

        Scene scene = new Scene(stripCell, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "test if styleclass was set correctly"() {
        given: "String of styleclass which shall be set"
        String styleClass = "strip-cell"

        when: "the cell is created"
        stripCell = new StripCell<>()

        then: "styleclass must be set"
        stripCell.getStyleClass().contains(styleClass)
    }

    def "test if maxWidth and Height were set correctly"() {
        given: "Double value of max and min which shall be set"
        double val = Double.MAX_VALUE;

        when: "the cell is created"
        stripCell = new StripCell<>()

        then: "max and min must be set"
        val == stripCell.getMaxWidth()
        val == stripCell.getMaxHeight()
    }

    def "test item listener"() {
        given: "mockmodule"
        WorkbenchModule workbenchModule = Mock()
        String expectedText = "modules_name"
        1 * workbenchModule.toString() >> expectedText
        robot.interact {
            mockSelectionStrip = new MockSelectionStrip()
        }

        when: "adding module"
        robot.interact {
            stripCell.setSelectionStrip(mockSelectionStrip)
            stripCell.setItem(workbenchModule)
        }

        then: "text must be set"
        expectedText == stripCell.getText()
        !stripCell.isSelected()

        when: "adding a module to the selectionStrip"
        robot.interact {
            mockSelectionStrip.setSelectedItem(workbenchModule)
        }

        then: "Selection has to change"
        expectedText == stripCell.getText()
        stripCell.isSelected()
    }

    def "test update selection"() {

    }

//    def "tests selecteditem listener: behaviour of adding a module and setting it selected"(
//            boolean autoscrolling, WorkbenchModule module, WorkbenchModule selectedModule, int listSize) {
//        given:
//        robot.interact {
//            selectionStrip.setAutoScrolling(autoscrolling)
//            selectionStrip.getItems().add(module)
//        }
//
//        expect:
//        robot.interact {
//            selectedModule == selectionStrip.getSelectedItem()
//            listSize == selectionStrip.getItems().size()
//            selectedModule == selectionStrip.getProperties().get("scroll.to");
//            mockSelectionStrip.selectedItemProperty().
//        }
//
//        where:
//        oldStrip | newStrip ||
//    }

    def "test listener when setting the selectionStrip"() {
        given: "SelectionStrip as Mock"
        robot.interact {
            mockSelectionStrip = new MockSelectionStrip()
        }

        when: "initial setup"
        robot.interact {
            stripCell.setSelectionStrip(mockSelectionStrip)
        }

        then: "selectionStripProperty is null"
        Objects.isNull(stripCell.getSelectionStrip())

        when: "old null and new null"
        robot.interact {
            stripCell.setSelectionStrip(null)
        }

        then: "nothing shall happen (selectionStripProperty is still null)"
        Objects.isNull(stripCell.getSelectionStrip())

        when: "old null and new !null"
        robot.interact {
//            stripCell.setSelectionStrip(mockSelectionStrip)
        }

        then: ""
        0 * mockSelectionStrip.selectedItemProperty()
        0 * property.addListener(_ as WeakInvalidationListener)
    }

    /*
    listener: 4 möglichkeiten -> selectionstrip-mock -> abfragen ob gecallt worden
    sttext aufgerufen?
    getitem.toString aufgerufen?

    wird getSelectedItem auf selectionstrip aufgerufen?
    ist aktuelles item selektiert?

    pseudoclassstate -> wird richtige gesetzt?
     */
}
