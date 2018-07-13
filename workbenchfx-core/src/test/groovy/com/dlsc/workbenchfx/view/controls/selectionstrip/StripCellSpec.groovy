package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class StripCellSpec extends ApplicationSpec {

    private StripCell<WorkbenchModule> stripCell
    private FxRobot robot

    @Override
    void start(Stage stage) throws Exception {
        stripCell = new StripCell<>()
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

    /*
    maxwitdh? maxheight?
    listener: 4 möglichkeiten -> selectionstrip-mock -> abfragen ob gecallt worden
    sttext aufgerufen?
    getitem.toString aufgerufen?

    wird getSelectedItem auf selectionstrip aufgerufen?
    ist aktuelles item selektiert?

    pseudoclassstate -> wird richtige gesetzt?
     */
}
