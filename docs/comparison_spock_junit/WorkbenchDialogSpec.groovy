import com.dlsc.workbenchfx.view.dialog.DialogMessageContent
import com.dlsc.workbenchfx.view.dialog.WorkbenchDialog
import com.dlsc.workbenchfx.view.dialog.WorkbenchDialog.Type
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

    def "Initialization of optional parameters"() {
        // test all optional parameters available
        // TODO
    }

    def "Initialization of a Dialog with Type #type has exactly the ButtonTypes #buttonTypes"(
            Type type, ButtonType[] buttonTypes) {
        given:
        dialog = WorkbenchDialog.builder(TITLE, content, type).build()

        expect:
        type == dialog.getType()
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
    }
}
