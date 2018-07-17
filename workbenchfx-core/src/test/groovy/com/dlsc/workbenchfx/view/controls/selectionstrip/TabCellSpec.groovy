package com.dlsc.workbenchfx.view.controls.selectionstrip

import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class TabCellSpec extends ApplicationSpec {

    private TabCell tabCell
    private FxRobot robot

    @Override
    void start(Stage stage) throws Exception {
        tabCell = new TabCell()

        robot = new FxRobot()
        Scene scene = new Scene(tabCell, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "test adding one module"() {
        given: "WorkbenchModule and SelectionStrip as mock"
//        SelectionStrip<WorkbenchModule> selectionStrip = Mock()
//        selectionStrip.selectedItemProperty() >> {
//            ObjectProperty<WorkbenchModule> objectProperty = Mock()
//            objectProperty.addListener(_) >> {
////                ???
//            }
//        }
//        WorkbenchModule workbenchModule = Mock()

        when: "the module is added"
//        robot.interact {
//            tabCell = new TabCell()
//            tabCell.setSelectionStrip(selectionStrip)
//            tabCell.setItem(workbenchModule)
//        }

        then: "text must be empty"
//        "" == tabCell.getText()
    }

    /*
    WorkbenchModule setzen
    Text muss "" sein
    Wir Workbench gecallt? (2 mocks)
    Tabfactory gemockt? (1 mock)
    Wird setModule aufgeruft?
    setGraphic?
     */

}
