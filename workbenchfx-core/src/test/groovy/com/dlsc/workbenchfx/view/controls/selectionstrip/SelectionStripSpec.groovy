package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.scene.control.Label
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
}
