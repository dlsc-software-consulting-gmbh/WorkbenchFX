import com.dlsc.workbenchfx.model.WorkbenchDialog
import com.dlsc.workbenchfx.model.WorkbenchDialog.Type
import com.dlsc.workbenchfx.view.controls.dialog.DialogMessageContent
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Unroll

@Unroll
class WorkbenchDialogSpec extends ApplicationSpec {
    private static final String TITLE = "Dialog Test Title"
    private static final String MESSAGE = "Dialog Test Message"
    private static final ButtonType[] BUTTON_TYPES =
            [ButtonType.PREVIOUS, ButtonType.NEXT] as ButtonType[]
    private Label content
    private static final WorkbenchDialog.Type TYPE = WorkbenchDialog.Type.INFORMATION
    private WorkbenchDialog dialog

    private FxRobot robot

    @Override
    void start(Stage stage) {
        robot = new FxRobot()
        content = new Label(MESSAGE)
        Scene scene = new Scene(content, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "Dialog Constructor - With type and content node"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE).build()

        then:
        dialog.getTitle() == TITLE
        dialog.getContent() == content
        dialog.getType() == TYPE
        dialog.getStyleClass().get(0) == TYPE.name().toLowerCase()
    }

    def "Dialog Constructor - With type and message"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, MESSAGE, TYPE).build()

        then:
        dialog.getTitle() == TITLE
        dialog.getContent() instanceof DialogMessageContent
        dialog.getType() == TYPE
        dialog.getStyleClass().get(0) == TYPE.name().toLowerCase()
    }

    def "Dialog Constructor - With button types and message"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, MESSAGE, BUTTON_TYPES).build()

        then:
        dialog.getTitle() == TITLE
        dialog.getContent() instanceof DialogMessageContent
        dialog.getType() == null
        dialog.getStyleClass().size() == 0
        dialog.getButtonTypes().size() == BUTTON_TYPES.length
        dialog.getButtonTypes().toArray() == BUTTON_TYPES
    }

    def "Dialog Constructor - With button types and content node"() {
        when:
        dialog = WorkbenchDialog.builder(TITLE, content, BUTTON_TYPES).build()

        then:
        dialog.getTitle() == TITLE
        dialog.getContent() == content
        dialog.getType() == null
        dialog.getStyleClass().size() == 0
        dialog.getButtonTypes().size() == BUTTON_TYPES.length
        dialog.getButtonTypes().toArray() == BUTTON_TYPES
    }

    def "Initialization of optional parameters - Defaults"() {
        when: "No optional parameters are specified"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE).build()

        then: "Defaults are set"
        !dialog.isBlocking()
        !dialog.isMaximized()
        dialog.isButtonsBarShown()
        Objects.isNull(dialog.getException())
        dialog.getDetails() == ""
    }

    def "Initialization of optional parameters - Specified"() {
        given: "Defined optional parameters"
        boolean blocking = true
        boolean maximized = true
        boolean showButtonsBar = false
        def styleClasses = ["first-style-class","second-style-class"] as String[]
        Exception exception = Stub(Exception.class)
        String details = "These are some details"

        when: "Optional parameters are specified"
        dialog = WorkbenchDialog.builder(TITLE, content, TYPE)
                .blocking(blocking)
                .maximized(maximized)
                .showButtonsBar(showButtonsBar)
                .styleClass(styleClasses)
                .exception(exception)
                .details(details)
                .build()

        then: "Specified optional parameters are correctly set"
        dialog.isBlocking() == blocking
        dialog.isMaximized() == maximized
        dialog.isButtonsBarShown() == showButtonsBar
        dialog.getStyleClass().containsAll(styleClasses)
        dialog.getException() == exception
        dialog.getDetails() == details
    }

    def "Initialization of a Dialog with Type #type has exactly the ButtonTypes #buttonTypes"(
            Type type, ButtonType[] buttonTypes) {
        given:
        dialog = WorkbenchDialog.builder(TITLE, content, type).build()

        expect:
        dialog.getType() == type
        dialog.getButtonTypes().size() == buttonTypes.length
        dialog.getButtonTypes().toArray() == buttonTypes

        where:
        type              | buttonTypes
        Type.INPUT        | [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]
        Type.INFORMATION  | [ButtonType.OK] as ButtonType[]
        Type.ERROR        | [ButtonType.CLOSE] as ButtonType[]
        Type.WARNING      | [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]
        Type.INPUT        | [ButtonType.OK, ButtonType.CANCEL] as ButtonType[]
        Type.CONFIRMATION | [ButtonType.YES, ButtonType.NO] as ButtonType[]
        null              | new ButtonType[0]
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
        dialog.getException() == exception
        dialog.getDetails() == details

        when: "Dialog's exception object is changed"
        dialog.setException(exception2)

        then: "Details gets updated with stacktrace of the new exception by exception listener"
        dialog.getException() == exception2
        dialog.getDetails() == details2

        when:
        dialog.setException(null)

        then: "Details are not updated"
        dialog.getException() == null
        dialog.getDetails() == details2
    }

    def setupMockException(Exception mock, String details) {
        1 * mock.printStackTrace((PrintWriter)_) >> {arguments ->
            PrintWriter printWriter = arguments[0] // capture PrintWriter that was used in the call
            printWriter.print(details) // mock behavior of Throwable#printStackTrace
        }
    }
}
