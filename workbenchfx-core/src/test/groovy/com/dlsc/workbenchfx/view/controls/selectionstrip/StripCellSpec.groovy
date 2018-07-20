package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.testing.MockSelectionStrip
import javafx.css.PseudoClass
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
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected")

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
        stripCell.getPseudoClassStates().contains(PSEUDO_CLASS_SELECTED)
    }

    def "test selectionStripListener"() {
        given: "selectionstrip was set"
        SelectionStrip<WorkbenchModule> mockStrip = new MockSelectionStrip()
        robot.interact {
            mockStrip.setSelectedItem(mockModule)
            stripCell.setSelectionStrip(mockSelectionStrip)
            stripCell.setItem(mockModule)
        }

        when: "a new selectionStrip with the same module is set"
        robot.interact {
            stripCell.setSelectionStrip(mockStrip)
        }

        then: "the selectionStripProperty listener triggers and executes updateSelection()"
        stripCell.isSelected()
        stripCell.getPseudoClassStates().contains(PSEUDO_CLASS_SELECTED)

        when: "a selectionStrip with null is set"
        robot.interact {
            stripCell.setSelectionStrip(null)
        }

        then: "nothing should happen, because no selectionstrip is set -> all old states are remaining"
        stripCell.isSelected()
        stripCell.getPseudoClassStates().contains(PSEUDO_CLASS_SELECTED)
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
        expectedText == stripCell.getText()
        isSelected == stripCell.isSelected()
        isSelected == stripCell.getPseudoClassStates().contains(PSEUDO_CLASS_SELECTED)
        stripCell == stripCell.selectedProperty().getBean()
        "selected" == stripCell.selectedProperty().getName()

        where:
        item       | selectedItem || expectedText     | isSelected
        null       | null         || ""               | true
        null       | mockModule   || ""               | false
        mockModule | mockModule   || expectedToString | true
        mockModule | null         || expectedToString | false
    }
}
