package com.dlsc.workbenchfx.view.dialog

import com.dlsc.workbenchfx.Workbench
import com.dlsc.workbenchfx.model.WorkbenchDialog
import com.dlsc.workbenchfx.view.controls.GlassPane
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Unroll

import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * Integration test for dialog related logic.
 */
@Unroll
class IDialogSpec extends ApplicationSpec {
    private static final String TITLE = "Dialog Test Title"
    private static final String MESSAGE = "Dialog Test Message"

    private Workbench workbench

    private EventHandler<Event> mockShownHandler
    private EventHandler<Event> mockHiddenHandler
    private Consumer<ButtonType> mockOnResultHandler

    private Stage stage

    @Override
    void start(Stage stage) {
        this.stage = stage

        workbench = new Workbench()

        Scene scene = new Scene(workbench, 100, 100)
        this.stage.setScene(scene)
        stage.show()
    }

    def "When pressing #keyPress on a #isBlocking dialog with #buttonsUsed #buttonsBarShown then #isDialogHidden #withResult"() {
        given:
        WorkbenchDialog dialog
        interact {
            mockShownHandler = Mock()
            mockHiddenHandler = Mock()
            mockOnResultHandler = Mock()
            dialog = WorkbenchDialog.builder(TITLE, MESSAGE, buttonTypes as ButtonType[])
                    .blocking(blocking)
                    .onShown(mockShownHandler)
                    .onHidden(mockHiddenHandler)
                    .onResult(mockOnResultHandler)
                    .showButtonsBar(showButtonsBar)
                    .build()
            workbench.showDialog(dialog)
            1 * mockShownHandler.handle((Event) _)
        }

        when: "Key is pressed"
        interact {
            push(keyPress)
        }

        then:
        dialogHidden == (amountDialogShowing() == 0)
        if (dialogHidden) {
                1 * mockHiddenHandler.handle((Event) _)
                1 * mockOnResultHandler.accept(_) >> { arguments ->
                    ButtonType buttonType = arguments[0] // capture ButtonType that was set as result
                    assert result == buttonType
            }
        }

        where:
        blocking | buttonTypes                                                             | keyPress       | showButtonsBar || dialogHidden | result
        false    | [ButtonType.OK, ButtonType.CANCEL]                                      | KeyCode.ENTER  | true           || true         | ButtonType.OK
        false    | [ButtonType.OK, ButtonType.CANCEL]                                      | KeyCode.ESCAPE | true           || true         | ButtonType.CANCEL
        false    | [ButtonType.CLOSE]                                                      | KeyCode.ENTER  | true           || true         | ButtonType.CLOSE
        false    | [ButtonType.CLOSE]                                                      | KeyCode.ESCAPE | true           || true         | ButtonType.CLOSE
        false    | [ButtonType.YES, ButtonType.NO]                                         | KeyCode.ENTER  | true           || true         | ButtonType.YES
        false    | [ButtonType.YES, ButtonType.NO]                                         | KeyCode.ESCAPE | true           || true         | ButtonType.NO
        false    | [ButtonType.OK]                                                         | KeyCode.ENTER  | true           || true         | ButtonType.OK
        false    | [ButtonType.OK]                                                         | KeyCode.ESCAPE | true           || true         | ButtonType.CANCEL
        false    | [ButtonType.OK]                                                         | KeyCode.ENTER  | false          || false        | _
        false    | [ButtonType.OK]                                                         | KeyCode.ESCAPE | false          || true         | ButtonType.CANCEL
        false    | []                                                                      | KeyCode.ENTER  | true           || false        | _
        false    | []                                                                      | KeyCode.ESCAPE | true           || true         | ButtonType.CANCEL
        true     | [ButtonType.OK, ButtonType.CANCEL]                                      | KeyCode.ENTER  | true           || true         | ButtonType.OK
        true     | [ButtonType.OK, ButtonType.CANCEL]                                      | KeyCode.ESCAPE | true           || false        | _
        true     | [ButtonType.CLOSE]                                                      | KeyCode.ENTER  | true           || true         | ButtonType.CLOSE
        true     | [ButtonType.CLOSE]                                                      | KeyCode.ESCAPE | true           || false        | _
        true     | [ButtonType.YES, ButtonType.NO]                                         | KeyCode.ENTER  | true           || true         | ButtonType.YES
        true     | [ButtonType.YES, ButtonType.NO]                                         | KeyCode.ESCAPE | true           || false        | _
        true     | [ButtonType.OK]                                                         | KeyCode.ENTER  | true           || true         | ButtonType.OK
        true     | [ButtonType.OK]                                                         | KeyCode.ESCAPE | true           || false        | _
        true     | [ButtonType.OK]                                                         | KeyCode.ENTER  | false          || false        | _
        true     | [ButtonType.OK]                                                         | KeyCode.ESCAPE | false          || false        | _
        true     | []                                                                      | KeyCode.ENTER  | true           || false        | _
        true     | []                                                                      | KeyCode.ESCAPE | true           || false        | _
        false    | [ButtonType.FINISH, ButtonType.CANCEL, ButtonType.OK, ButtonType.CLOSE] | KeyCode.ENTER  | true           || true         | ButtonType.FINISH
        false    | [ButtonType.FINISH, ButtonType.CANCEL, ButtonType.OK, ButtonType.CLOSE] | KeyCode.ESCAPE | true           || true         | ButtonType.CLOSE

        isBlocking = blocking ? "blocking" : "non-blocking"
        buttonsBarShown = showButtonsBar ? "" : "with no buttons showing,"
        isDialogHidden = dialogHidden ? "the dialog gets hidden" : "the dialog stays visible"
        prettyButtonTypes = buttonTypes.stream().map{ it -> it.getText() }.collect(Collectors.joining(" and "))
        buttonsUsed = buttonTypes.size() == 0 ? "no buttons," : prettyButtonTypes + ","
        withResult = result == _ ? "" : "with " + result.getText()
    }

    def "When clicking on the GlassPane of a #isBlocking dialog with #buttonsUsed #buttonsBarShown then #isDialogHidden #withResult"() {
        given:
        WorkbenchDialog dialog
        interact {
            mockShownHandler = Mock()
            mockHiddenHandler = Mock()
            mockOnResultHandler = Mock()
            dialog = WorkbenchDialog.builder(TITLE, MESSAGE, buttonTypes as ButtonType[])
                    .blocking(blocking)
                    .onShown(mockShownHandler)
                    .onHidden(mockHiddenHandler)
                    .onResult(mockOnResultHandler)
                    .showButtonsBar(showButtonsBar)
                    .build()
            workbench.showDialog(dialog)
            1 * mockShownHandler.handle((Event) _)
        }

        when: "GlassPane is clicked"
        interact {
            simulateGlassPaneClick(dialog.getDialogControl())
        }

        then:
        dialogHidden == (amountDialogShowing() == 0)
        if (dialogHidden) {
            1 * mockHiddenHandler.handle((Event) _)
            1 * mockOnResultHandler.accept(_) >> { arguments ->
                ButtonType buttonType = arguments[0] // capture ButtonType that was set as result
                assert result == buttonType
            }
        }

        where:
        blocking | buttonTypes                        | showButtonsBar || dialogHidden | result
        false    | [ButtonType.OK, ButtonType.CANCEL] | true           || true         | ButtonType.CANCEL
        false    | [ButtonType.CLOSE]                 | true           || true         | ButtonType.CLOSE
        false    | [ButtonType.YES, ButtonType.NO]    | true           || true         | ButtonType.NO
        false    | [ButtonType.OK]                    | true           || true         | ButtonType.CANCEL
        false    | [ButtonType.OK]                    | false          || true         | ButtonType.CANCEL
        false    | []                                 | true           || true         | ButtonType.CANCEL
        true     | [ButtonType.OK, ButtonType.CANCEL] | true           || false        | _
        true     | [ButtonType.CLOSE]                 | true           || false        | _
        true     | [ButtonType.YES, ButtonType.NO]    | true           || false        | _
        true     | [ButtonType.OK]                    | true           || false        | _
        true     | [ButtonType.OK]                    | false          || false        | _
        true     | []                                 | true           || false        | _

        isBlocking = blocking ? "blocking" : "non-blocking"
        buttonsBarShown = showButtonsBar ? "" : "with no buttons showing,"
        isDialogHidden = dialogHidden ? "the dialog gets hidden" : "the dialog stays visible"
        prettyButtonTypes = buttonTypes.stream().map{ it -> it.getText() }.collect(Collectors.joining(" and "))
        buttonsUsed = buttonTypes.size() == 0 ? "no buttons," : prettyButtonTypes + ","
        withResult = result == _ ? "" : "with " + result.getText()
    }

    /**
     * Internal testing method that will simulate a click on a {@link com.dlsc.workbenchfx.view.controls.GlassPane} of
     * an {@code overlayNode}.
     *
     * @param overlayNode of which the GlassPane should be clicked
     */
    def simulateGlassPaneClick(Node overlayNode) {
        GlassPane glassPane = workbench.getOverlays().get(overlayNode).getGlassPane();
        glassPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1,
                false, false, false, false, true, false, false, false, false, false,
                null)
        );
    }

    def amountDialogShowing() {
        return workbench.getBlockingOverlaysShown().size() + workbench.getNonBlockingOverlaysShown().size()
    }

}
