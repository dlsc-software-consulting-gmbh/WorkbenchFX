package com.dlsc.workbenchfx.view.controls.selectionstrip

import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.testing.MockSelectionStrip
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Callback
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Shared
import spock.lang.Unroll

class SelectionStripSpec extends ApplicationSpec {

    private SelectionStrip<WorkbenchModule> selectionStrip
    private FxRobot robot

    @Shared
    private WorkbenchModule workbenchModule = Mock()
    @Shared
    private WorkbenchModule workbenchModule2 = Mock()

    @Override
    void start(Stage stage) throws Exception {
        selectionStrip = new MockSelectionStrip()
        robot = new FxRobot()

        Scene scene = new Scene(selectionStrip, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "test if styleclass was set correctly"() {
        given: "String of styleclass which shall be set"
        String styleClass = "selection-strip"

        when: "selectionStrip was initialized"

        then: "styleclass must be set"
        selectionStrip.getStyleClass().contains(styleClass)
    }

    def "test if maxWidth and Height were set correctly"() {
        given: "Double value of max and min which shall be set"
        double prefWidth = 400;
        double prefHeight = 50;

        when: "selectionStrip was initialized"

        then: "max and min must be set"
        prefWidth == selectionStrip.getPrefWidth()
        prefHeight == selectionStrip.getPrefHeight()
    }

    def "set a cell factory"() {
        when: "selectionStrip was initialized"

        then: "cellfactory is instance of StripCell"
        selectionStrip.getCellFactory() instanceof Callback<SelectionStrip, StripCell<WorkbenchModule>>
    }

    @Unroll
    def "tests selected-item listener: autoScrolling: '#autoScrolling' and selectedItem adding: '#selectedItem' => autoscrolledModule: '#autoscrolledModule'"(
            boolean autoScrolling,
            WorkbenchModule selectedItem,
            WorkbenchModule autoscrolledModule
    ) {
        given:
        robot.interact {
            selectionStrip.setAutoScrolling(autoScrolling)
            selectionStrip.setSelectedItem(selectedItem)
        }

        expect:
        autoscrolledModule == selectionStrip.getProperties().get("scroll.to");

        where:
        autoScrolling | selectedItem    || autoscrolledModule
        true          | null            || null
        true          | workbenchModule || workbenchModule
        false         | null            || null
        false         | workbenchModule || null
    }
}
