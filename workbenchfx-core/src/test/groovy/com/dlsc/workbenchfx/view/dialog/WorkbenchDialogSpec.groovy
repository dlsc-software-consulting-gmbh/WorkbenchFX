import com.dlsc.workbenchfx.model.WorkbenchDialog
import com.dlsc.workbenchfx.model.WorkbenchDialog.Type
import com.dlsc.workbenchfx.testing.MockDialogControl
import com.dlsc.workbenchfx.view.controls.MultilineLabel
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Unroll

import java.util.function.Consumer

@Unroll
class WorkbenchDialogSpec extends ApplicationSpec {
    private static final String TITLE = "Dialog Test Title"
    private static final String MESSAGE = "Dialog Test Message"
    private static final ButtonType[] BUTTON_TYPES =
            [ButtonType.PREVIOUS, ButtonType.NEXT] as ButtonType[]
    private Label content
    private static final WorkbenchDialog.Type TYPE = WorkbenchDialog.Type.INFORMATION
    private WorkbenchDialog dialog
    private MockDialogControl mockDialogControl;

    private FxRobot robot

    @Override
    void start(Stage stage) {
        robot = new FxRobot()
        content = new Label(MESSAGE)
        mockDialogControl = new MockDialogControl()
        Scene scene = new Scene(content, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "Dialog Constructor - With type and content node"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE).build()

        then:
        TITLE == dialog.getTitle()
        content == dialog.getContent()
        TYPE == dialog.getType()
        TYPE.name().toLowerCase() == dialog.getStyleClass().get(0)
    }

    def "Dialog Constructor - With type and message"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, MESSAGE, TYPE).build()

        then:
        TITLE == dialog.getTitle()
        dialog.getContent() instanceof MultilineLabel
        TYPE == dialog.getType()
        TYPE.name().toLowerCase() == dialog.getStyleClass().get(0)
    }

    def "Dialog Constructor - With button types and message"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, MESSAGE, BUTTON_TYPES).build()

        then:
        TITLE == dialog.getTitle()
        dialog.getContent() instanceof MultilineLabel
        null == dialog.getType()
        0 == dialog.getStyleClass().size()
        BUTTON_TYPES.length == dialog.getButtonTypes().size()
        BUTTON_TYPES == dialog.getButtonTypes().toArray()
    }

    def "Dialog Constructor - With button types and content node"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, content, BUTTON_TYPES).build()

        then:
        TITLE == dialog.getTitle()
        content == dialog.getContent()
        null == dialog.getType()
        0 == dialog.getStyleClass().size()
        BUTTON_TYPES.length == dialog.getButtonTypes().size()
        BUTTON_TYPES == dialog.getButtonTypes().toArray()
    }

    def "Initialization of optional parameters - Defaults"() {
        when: "No optional parameters are specified"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE).build()

        then: "Defaults are set"
        !dialog.isBlocking()
        !dialog.isMaximized()
        dialog.isButtonsBarShown()
        null == dialog.getException()
        "" == dialog.getDetails()
        null != dialog.getOnResult()
        null != dialog.getDialogControl()
        null == dialog.getOnHidden()
        null == dialog.getOnShown()
    }

    def "Initialization of optional parameters - Specified"() {
        given: "Defined optional parameters"
        boolean blocking = true
        boolean maximized = true
        boolean showButtonsBar = false
        def styleClasses = ["first-style-class", "second-style-class"] as String[]
        Exception exception = Stub(Exception.class)
        String details = "These are some details"
        Consumer<ButtonType> onResult = { buttonType -> }
        EventHandler<Event> onHidden = { event -> }
        EventHandler<Event> onShown = { event -> }
        DialogControl dialogControl = new MockDialogControl()

        when: "Optional parameters are specified"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .blocking(blocking)
                .maximized(maximized)
                .showButtonsBar(showButtonsBar)
                .styleClass(styleClasses)
                .exception(exception)
                .details(details)
                .onResult(onResult)
                .onHidden(onHidden)
                .onShown(onShown)
                .dialogControl(dialogControl)
                .build()

        then: "Specified optional parameters are correctly set"
        blocking == dialog.isBlocking()
        maximized == dialog.isMaximized()
        showButtonsBar == dialog.isButtonsBarShown()
        dialog.getStyleClass().containsAll(styleClasses)
        exception == dialog.getException()
        details == dialog.getDetails()
        onResult == dialog.getOnResult()
        onHidden == dialog.getOnHidden()
        onShown == dialog.getOnShown()
        dialogControl == dialog.getDialogControl()
    }

    def "Initialization of a Dialog with Type #type has exactly the ButtonTypes #buttonTypes"(
            Type type, ButtonType[] buttonTypes) {
        given:
        dialog = WorkbenchDialog.builder(TITLE, content, type).build()

        expect:
        type == dialog.getType()
        buttonTypes.length == dialog.getButtonTypes().size()
        buttonTypes == dialog.getButtonTypes().toArray()

        where:
        type              || buttonTypes
        Type.INPUT        || [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]
        Type.INFORMATION  || [ButtonType.OK] as ButtonType[]
        Type.ERROR        || [ButtonType.CLOSE] as ButtonType[]
        Type.WARNING      || [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]
        Type.INPUT        || [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]
        Type.CONFIRMATION || [ButtonType.YES, ButtonType.NO] as ButtonType[]
        null              || new ButtonType[0]
    }

    def "Exception listener correctly sets details"() {
        given:
        Exception exception = Mock(Exception.class)
        Exception exception2 = Mock(Exception.class)
        String details = "Stacktrace of Exception"
        String details2 = "Another " + details
        setupMockException(exception, details)
        setupMockException(exception2, details2)

        when: "Dialog with exception is created via a builder"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .exception(exception)
                .build()

        then: "Specified optional parameters are correctly set"
        exception == dialog.getException()
        details == dialog.getDetails()

        when: "Dialog's exception object is changed"
        dialog.setException(exception2)

        then: "Details gets updated with stacktrace of the new exception by exception listener"
        exception2 == dialog.getException()
        details2 == dialog.getDetails()

        when:
        dialog.setException(null)

        then: "Details are not updated"
        null == dialog.getException()
        details2 == dialog.getDetails()
    }

    def setupMockException(Exception mock, String details) {
        1 * mock.printStackTrace((PrintWriter) _) >> { arguments ->
            PrintWriter printWriter = arguments[0] // capture PrintWriter that was used in the call
            printWriter.print(details) // mock behavior of Throwable#printStackTrace
        }
    }

    def "Test getButton"() {
        given: "WorkbenchDialog with dialogControl as null"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .dialogControl(null)
                .build()

        when: "getButton specific for TYPE"
        Optional<Button> button = dialog.getButton(ButtonType.OK)

        then: "Since dialogControl is null, empty Optional is returned"
        Optional.empty() == button

        when: "DialogControl is defined"
        dialog.setDialogControl(mockDialogControl)
        button = dialog.getButton(ButtonType.OK)

        then: "Button is returned of DialogControl"
        Optional.empty() != button
        mockDialogControl.getButtons().get(0) == button.get()

        when: "WorkbenchDialog has no ButtonTypes"
        dialog.getButtonTypes().clear()
        button = dialog.getButton(ButtonType.OK)

        then:
        Optional.empty() == button
    }

    def "Test delegation of onShownProperty"() {
        given: "WorkbenchDialog with dialogControl as null"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .dialogControl(null)
                .build()

        when:
        dialog.onShownProperty()

        then: "Since DialogControl is not set yet, it will throw an NPE"
        thrown NullPointerException

        when: "DialogControl is defined"
        dialog.setDialogControl(mockDialogControl)
        def onShownProperty = dialog.onShownProperty()

        then:
        mockDialogControl.onShownProperty() == onShownProperty
    }

    def "Test delegation of onHiddenProperty"() {
        given: "WorkbenchDialog with dialogControl as null"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .dialogControl(null)
                .build()

        when:
        dialog.onHiddenProperty()

        then: "Since DialogControl is not set yet, it will throw an NPE"
        thrown NullPointerException

        when: "DialogControl is defined"
        dialog.setDialogControl(mockDialogControl)
        def onHiddenProperty = dialog.onHiddenProperty()

        then:
        mockDialogControl.onHiddenProperty() == onHiddenProperty
    }

    def "Test setOnResult"() {
        given: "WorkbenchDialog with dialogControl as null"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .build()

        when: "Setting null as onResult"
        dialog.setOnResult(null)

        then: "Instead of null, an empty consumer has been set"
        null != dialog.getOnResult()

        when: "Setting a proper consumer"
        Consumer<ButtonType> onResult = { buttonType -> System.out.println("Test") }
        dialog.setOnResult(onResult)

        then:
        onResult == dialog.getOnResult()
    }
}
