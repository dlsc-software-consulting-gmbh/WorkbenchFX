package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.testing.MockSelectionStrip
import javafx.beans.WeakInvalidationListener
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Shared
import spock.lang.Unroll

class StripCellSpec extends ApplicationSpec {

    @Shared
    private WorkbenchModule mockModule = Mock()
    @Shared
    private String expectedToString = "mock_module"

    @Override
    FxRobot clickOn(MouseButton... param0) {
        return super.clickOn(param0)
    }
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

    def setupSpec() {
        mockModule.toString() >> expectedToString
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

    def "test selectionStripSelectionListener"() {

        when: "the selected item of the selectionStrip changes"
        robot.interact {
            stripCell.setSelectionStrip(mockSelectionStrip)
            stripCell.setItem(mockModule)
            mockSelectionStrip.setSelectedItem(mockModule)
        }

        then: "the selectionlistener triggers and executes updateSelection()"
        stripCell.isSelected()
    }

    @Unroll
    def "test item listener: '#item' as item, '#selectedItem' as selected item => '#expectedText' as expectedText, '#isSelected' as selection state"(
            WorkbenchModule item,
            WorkbenchModule selectedItem,
            String expectedText,
            boolean isSelected
    ) {
        given:
        robot.interact {
            mockSelectionStrip.setSelectedItem(selectedItem)
            stripCell.setSelectionStrip(mockSelectionStrip)
            stripCell.setItem(item)
        }

        expect:
        robot.interact {
            expectedText == stripCell.getText()
            isSelected == stripCell.isSelected()
        }

        where:
        item       | selectedItem || expectedText     | isSelected
        null       | null         || ""               | false
        null       | mockModule   || ""               | false
        mockModule | mockModule   || expectedToString | true
        mockModule | null         || expectedToString | false
    }

    def "test update selection"() {

    }

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
