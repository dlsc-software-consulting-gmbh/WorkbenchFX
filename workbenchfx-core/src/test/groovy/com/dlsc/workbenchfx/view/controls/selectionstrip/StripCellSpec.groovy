package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.beans.WeakInvalidationListener
import javafx.beans.property.ObjectProperty
import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class StripCellSpec extends ApplicationSpec {

    private StripCell<WorkbenchModule> stripCell
//    ObjectProperty<WorkbenchModule> property
//    SelectionStrip<WorkbenchModule> selectionStrip
    private FxRobot robot

    @Override
    void start(Stage stage) throws Exception {
        stripCell = new StripCell<>()
//        property = Mock()
//        property.addListener(_ as WeakInvalidationListener)
//        selectionStrip = Mock()
//        selectionStrip.selectedItemProperty() >> property

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

//    def "Initialization of a Dialog with Type #type has exactly the ButtonTypes #buttonTypes"(
//            Type type, ButtonType[] buttonTypes) {
//        given:
//        dialog = WorkbenchDialog.builder(TITLE, content, type).build()
//
//        expect:
//        type == dialog.getType()
//        buttonTypes.length == dialog.getButtonTypes().size()
//        buttonTypes == dialog.getButtonTypes().toArray()
//
//        where:
//        type              || buttonTypes
//        Type.INPUT        || [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]

    def "test listener when setting the selectionStrip"() {
        given: "SelectionStrip as Mock"
        ObjectProperty<WorkbenchModule> property = Mock()
        SelectionStrip<WorkbenchModule> selectionStrip = Mock()
        selectionStrip.selectedItemProperty() >> { args -> property }

        when: "initial setup"
        robot.interact {
            stripCell = new StripCell<>()
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
            stripCell.setSelectionStrip(selectionStrip)
        }

        then: ""
        1 * selectionStrip.selectedItemProperty()
        1 * property.addListener(_ as WeakInvalidationListener)
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
