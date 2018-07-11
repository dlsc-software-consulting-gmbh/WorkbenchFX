package com.dlsc.workbenchfx.view.controls.selectionstrip

import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class TabCellSpec extends ApplicationSpec {

    private TabCell tabCell = new TabCell()
    private FxRobot robot = new FxRobot()

    @Override
    void start(Stage stage) throws Exception {
        Scene scene = new Scene(tabCell, 100, 100)
        stage.setScene(scene)
        stage.show()
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
