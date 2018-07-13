package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.stage.Stage
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
