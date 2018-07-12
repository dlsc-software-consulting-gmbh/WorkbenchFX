package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class SelectionStripSpec extends ApplicationSpec {

    private SelectionStrip<WorkbenchModule> selectionStrip = new SelectionStrip<>()
    private FxRobot robot = new FxRobot()

    @Override
    void start(Stage stage) throws Exception {
        Scene scene = new Scene(selectionStrip, 100, 100)
        stage.setScene(scene)
        stage.show()
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
