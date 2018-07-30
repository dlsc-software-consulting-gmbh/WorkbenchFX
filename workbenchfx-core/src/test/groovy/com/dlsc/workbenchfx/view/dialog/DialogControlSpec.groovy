package com.dlsc.workbenchfx.view.dialog

import com.dlsc.workbenchfx.Workbench
import com.dlsc.workbenchfx.model.WorkbenchDialog
import com.dlsc.workbenchfx.testing.MockDialogControl
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Unroll

@Unroll
class DialogControlSpec extends ApplicationSpec {
    private static final String TITLE = "Dialog Test Title"
    private static final String MESSAGE = "Dialog Test Message"
    private static final ButtonType[] BUTTON_TYPES =
            [ButtonType.PREVIOUS, ButtonType.NEXT] as ButtonType[]

    private FxRobot robot
    private Workbench workbench

    private WorkbenchDialog dialog

    private DialogControl dialogControl

    private EventHandler<Event> mockShownHandler
    private EventHandler<Event> mockHiddenHandler
    private EventHandler<Event> mockShownHandler2
    private EventHandler<Event> mockHiddenHandler2

    private Stage stage
    private DialogControl dialogControl2

    @Override
    void start(Stage stage) {
        this.stage = stage

        robot = new FxRobot()

        mockShownHandler = Mock()
        mockHiddenHandler = Mock()
        mockShownHandler2 = Mock()
        mockHiddenHandler2 = Mock()

        dialog = WorkbenchDialog.builder(TITLE, MESSAGE, BUTTON_TYPES).build()

        workbench = new Workbench()

        dialogControl = new MockDialogControl()

        // simulate call of workbench to set itself in the dialogControl
        dialogControl.setWorkbench(workbench)
        // simulate call of WorkbenchDialog to set itself in the dialogControl
        dialogControl.setDialog(dialog)

        // setup mocks for listeners
        dialogControl.setOnHidden(mockHiddenHandler)
        dialogControl.setOnShown(mockShownHandler)

        // setup second dialog control that isn't showing, to test behavior of skin listeners
        dialogControl2 = new MockDialogControl()
        dialogControl2.setDialog(dialog)
        dialogControl2.setOnShown(mockShownHandler2)
        dialogControl2.setOnHidden(mockHiddenHandler2)

        Scene scene = new Scene(dialogControl, 100, 100)
        this.stage.setScene(scene)
        stage.show()
    }

   def "Test if setup is working"() {
       when:
       def size = dialogControl.getButtons().size()

       then:
       size == 2
   }
}
