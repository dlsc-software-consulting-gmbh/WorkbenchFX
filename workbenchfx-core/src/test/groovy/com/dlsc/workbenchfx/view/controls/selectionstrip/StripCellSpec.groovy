package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import javafx.scene.Scene
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec

class StripCellSpec extends ApplicationSpec {

    private StripCell<WorkbenchModule> stripCell = new StripCell<>()
    private FxRobot robot = new FxRobot()

    @Override
    void start(Stage stage) throws Exception {
        Scene scene = new Scene(stripCell, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    /*
    Wurde styleclasse gesetzt? richtig gesetzt?
    maxwitdh? maxheight?
    listener: 4 möglichkeiten -> selectionstrip-mock -> abfragen ob gecallt worden
    sttext aufgerufen?
    getitem.toString aufgerufen?

    wird getSelectedItem auf selectionstrip aufgerufen?
    ist aktuelles item selektiert?

     */
}
