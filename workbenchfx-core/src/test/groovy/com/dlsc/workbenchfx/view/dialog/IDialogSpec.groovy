package com.dlsc.workbenchfx.view.dialog

import com.dlsc.workbenchfx.Workbench
import com.dlsc.workbenchfx.model.WorkbenchDialog
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Unroll

@Unroll
/**
 * Integration test for dialog related logic.
 */
class IDialogSpec extends ApplicationSpec {
    private static final String TITLE = "Dialog Test Title"
    private static final String MESSAGE = "Dialog Test Message"
    private static final ButtonType[] BUTTON_TYPES =
            [ButtonType.PREVIOUS, ButtonType.NEXT] as ButtonType[]

    private FxRobot robot
    private Workbench workbench

    private EventHandler<Event> mockShownHandler
    private EventHandler<Event> mockHiddenHandler

    private Stage stage

    @Override
    void start(Stage stage) {
        this.stage = stage

        robot = new FxRobot()

        mockShownHandler = Mock()
        mockHiddenHandler = Mock()

        workbench = new Workbench()

        Scene scene = new Scene(workbench, 100, 100)
        this.stage.setScene(scene)
        stage.show()
    }

    def "Test"() {
        given:
        WorkbenchDialog dialog = WorkbenchDialog.builder(TITLE, MESSAGE, buttonTypes)
                .blocking(blocking)
                .onShown(mockShownHandler)
                .onHidden(mockHiddenHandler)
                .build()
        workbench.showDialog(dialog)

        expect:


        where:
        blocking | buttonTypes                        || escCloseable | escResult         | enterCloseable | enterResult
        false    | [ButtonType.OK, ButtonType.CANCEL] || true         | ButtonType.CANCEL | true           | ButtonType.OK
    }

}
