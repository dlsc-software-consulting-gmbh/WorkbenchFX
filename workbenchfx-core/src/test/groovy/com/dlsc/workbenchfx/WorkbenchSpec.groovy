package com.dlsc.workbenchfx

import com.dlsc.workbenchfx.model.WorkbenchDialog
import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.testing.*
import com.dlsc.workbenchfx.view.controls.Dropdown
import com.dlsc.workbenchfx.view.controls.GlassPane
import com.dlsc.workbenchfx.view.controls.MultilineLabel
import com.dlsc.workbenchfx.view.controls.dialog.DialogErrorContent
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.spockframework.util.ExceptionUtil
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Shared
import spock.lang.Unroll

import java.util.function.Consumer

import static com.dlsc.workbenchfx.model.WorkbenchDialog.Type

@Unroll
class WorkbenchSpec extends ApplicationSpec {


    private static final int MODULE_AMOUNT = 3

    private static final double DRAWER_MAX_FACTOR = 0.9

    private static final int FIRST_INDEX = 0
    private static final int SECOND_INDEX = 1
    private static final int LAST_INDEX = MODULE_AMOUNT - 1

    private static final String TITLE = "Title of a Dialog"
    private static final String MESSAGE = "Message of a Dialog"
    private static final String DETAILS = "Details of a Dialog"
    private static final Exception EXCEPTION = new Exception()
    private static final Consumer<ButtonType> ON_RESULT = { buttonType ->  }
    public static final int SIZE = 100

    Workbench workbench

    WorkbenchModule[] mockModules = new WorkbenchModule[MODULE_AMOUNT]
    Node[] moduleNodes = new Node[MODULE_AMOUNT]

    WorkbenchModule first
    WorkbenchModule second
    WorkbenchModule last
    private ObservableMap<Node, GlassPane> overlays
    private ObservableSet<Node> blockingOverlaysShown
    private ObservableSet<Node> overlaysShown
    private Node overlay1
    private Node overlay2
    private Node overlay3

    private MenuItem menuItem
    private ObservableList<MenuItem> navigationDrawerItems

    @Shared private FxRobot robot = new FxRobot()

    // Dropdown items
    private String dropdownText
    private FontAwesomeIconView dropdownIconView
    private ImageView dropdownImageView
    private MenuItem dropdownMenuItem
    private Dropdown dropdownLeft
    private Dropdown dropdownRight

    private MockNavigationDrawer navigationDrawer
    private MockDialogControl dialogControl

    private static final int PERCENTAGE_HALF = 50

    @Shared Label drawerFillLabel

    @Override
    void start(Stage stage) {
        drawerFillLabel = new Label()
        drawerFillLabel.setMinSize(SIZE, SIZE)

        for (int i = 0; i < moduleNodes.length; i++) {
            moduleNodes[i] = new Label("Module Content")
        }

        for (int i = 0; i < mockModules.length; i++) {
            mockModules[i] = createMockModule(moduleNodes[i], null, true, "Module " + i)
        }

        FontAwesomeIconView fontAwesomeIconView = new FontAwesomeIconView(FontAwesomeIcon.QUESTION)
        fontAwesomeIconView.getStyleClass().add("icon")
        menuItem = new MenuItem("Item 1.1", fontAwesomeIconView)

        // Initialization of items for Dropdown testing
        dropdownText = "Dropdown Text"
        dropdownIconView = new FontAwesomeIconView(FontAwesomeIcon.QUESTION)
        dropdownImageView = new ImageView(
                new Image(WorkbenchTest.class.getResource("date-picker.png").toExternalForm())
        )
        dropdownMenuItem = new MenuItem("Menu Item")

        dropdownLeft = Dropdown.of(dropdownText, dropdownIconView, dropdownMenuItem)
        dropdownRight = Dropdown.of(dropdownText, dropdownImageView, dropdownMenuItem)

        navigationDrawer = new MockNavigationDrawer()
        dialogControl = new MockDialogControl()

        workbench = Workbench.builder(
                mockModules[FIRST_INDEX],
                mockModules[SECOND_INDEX],
                mockModules[LAST_INDEX])
                .tabFactory { workbench -> new MockTab(workbench) }
                .tileFactory { workbench -> new MockTile(workbench) }
                .pageFactory({ workbench -> new MockPage(workbench) })
                .navigationDrawer(navigationDrawer)
                .navigationDrawerItems(menuItem)
                .toolbarLeft(dropdownLeft)
                .toolbarRight(dropdownRight)
                .build()

        first = mockModules[FIRST_INDEX]
        second = mockModules[SECOND_INDEX]
        last = mockModules[LAST_INDEX]

        overlays = workbench.getOverlays()
        blockingOverlaysShown = workbench.getBlockingOverlaysShown()
        overlaysShown = workbench.getNonBlockingOverlaysShown()
        overlay1 = new Label()
        overlay1.setVisible(false)
        overlay2 = new Label()
        overlay2.setVisible(false)
        overlay3 = new Label()
        overlay3.setVisible(false)

        navigationDrawerItems = workbench.getNavigationDrawerItems()

        Scene scene = new Scene(workbench, SIZE, SIZE)
        stage.setScene(scene)
        stage.show()
    }

    def "Test #methodName(#arguments)"() {
        given:
        WorkbenchDialog dialog
        robot.interact {
            dialog = workbench."$methodName"(arguments)
        }

        expect:
        type == dialog.getType()
        TITLE == dialog.getTitle()
        if (type == Type.ERROR) {
            assert MESSAGE == ((MultilineLabel) ((DialogErrorContent) dialog.getContent()).getMessage()).getMessage()
            assert details == ((DialogErrorContent) dialog.getContent()).getDetails()
        } else {
            assert MESSAGE == ((MultilineLabel) dialog.getContent()).getMessage()
        }
        exception == dialog.getException()
        details == dialog.getDetails()
        ON_RESULT == dialog.getOnResult()

        where:
        methodName               | arguments                              || type              | exception | details
        "showErrorDialog"        | [TITLE, MESSAGE, ON_RESULT]            || Type.ERROR        | null      | ""
        "showErrorDialog"        | [TITLE, MESSAGE, DETAILS, ON_RESULT]   || Type.ERROR        | null      | DETAILS
        "showErrorDialog"        | [TITLE, MESSAGE, EXCEPTION, ON_RESULT] || Type.ERROR        | EXCEPTION | ExceptionUtil.printStackTrace(exception)
        "showWarningDialog"      | [TITLE, MESSAGE, ON_RESULT]            || Type.WARNING      | null      | ""
        "showConfirmationDialog" | [TITLE, MESSAGE, ON_RESULT]            || Type.CONFIRMATION | null      | ""
        "showInformationDialog"  | [TITLE, MESSAGE, ON_RESULT]            || Type.INFORMATION  | null      | ""
    }

    @Unroll
    def "Test #showDrawerCall"() {
        given:
        null == StackPane.getAlignment(drawer)
        !drawer.minWidthProperty().isBound()
        !drawer.minHeightProperty().isBound()
        !drawer.maxWidthProperty().isBound()
        !drawer.maxHeightProperty().isBound()
        workbench.getBlockingOverlaysShown().isEmpty()
        workbench.getNonBlockingOverlaysShown().isEmpty()
        robot.interact {
            workbench."showDrawer"(arguments)
        }

        expect:
        position == StackPane.getAlignment(drawer)
        width == drawer.getWidth()
        height == drawer.getHeight()
        minWidthBound == drawer.minWidthProperty().isBound()
        maxWidthBound == drawer.minHeightProperty().isBound()
        minHeightBound == drawer.maxWidthProperty().isBound()
        maxHeightBound == drawer.maxHeightProperty().isBound()
        workbench.getBlockingOverlaysShown().isEmpty()
        1 == workbench.getNonBlockingOverlaysShown().size()

        where:
        drawer                  | arguments                              || position        | width                    | height                   | minWidthBound | maxWidthBound | minHeightBound | maxHeightBound
        new VBox()              | [drawer, Side.TOP]                     || Pos.TOP_LEFT    | SIZE                     | drawer.prefHeight(-1)    | true          | false         | false          | true
        new VBox()              | [drawer, Side.RIGHT]                   || Pos.TOP_RIGHT   | drawer.prefWidth(-1)     | SIZE                     | false         | true          | true           | false
        new VBox()              | [drawer, Side.BOTTOM]                  || Pos.BOTTOM_LEFT | SIZE                     | drawer.prefHeight(-1)    | true          | false         | false          | true
        new VBox()              | [drawer, Side.LEFT]                    || Pos.TOP_LEFT    | drawer.prefWidth(-1)     | SIZE                     | false         | true          | true           | false
        createFullCoverDrawer() | [drawer, Side.TOP]                     || Pos.TOP_LEFT    | SIZE                     | SIZE * DRAWER_MAX_FACTOR | true          | false         | false          | true
        createFullCoverDrawer() | [drawer, Side.RIGHT]                   || Pos.TOP_RIGHT   | SIZE * DRAWER_MAX_FACTOR | SIZE                     | false         | true          | true           | false
        createFullCoverDrawer() | [drawer, Side.BOTTOM]                  || Pos.BOTTOM_LEFT | SIZE                     | SIZE * DRAWER_MAX_FACTOR | true          | false         | false          | true
        createFullCoverDrawer() | [drawer, Side.LEFT]                    || Pos.TOP_LEFT    | SIZE * DRAWER_MAX_FACTOR | SIZE                     | false         | true          | true           | false
        new VBox()              | [drawer, Side.TOP, -1]                 || Pos.TOP_LEFT    | SIZE                     | drawer.prefHeight(-1)    | true          | false         | false          | true
        new VBox()              | [drawer, Side.RIGHT, -1]               || Pos.TOP_RIGHT   | drawer.prefWidth(-1)     | SIZE                     | false         | true          | true           | false
        new VBox()              | [drawer, Side.BOTTOM, -1]              || Pos.BOTTOM_LEFT | SIZE                     | drawer.prefHeight(-1)    | true          | false         | false          | true
        new VBox()              | [drawer, Side.LEFT, -1]                || Pos.TOP_LEFT    | drawer.prefWidth(-1)     | SIZE                     | false         | true          | true           | false
        createFullCoverDrawer() | [drawer, Side.TOP, -1]                 || Pos.TOP_LEFT    | SIZE                     | SIZE * DRAWER_MAX_FACTOR | true          | false         | false          | true
        createFullCoverDrawer() | [drawer, Side.RIGHT, -1]               || Pos.TOP_RIGHT   | SIZE * DRAWER_MAX_FACTOR | SIZE                     | false         | true          | true           | false
        createFullCoverDrawer() | [drawer, Side.BOTTOM, -1]              || Pos.BOTTOM_LEFT | SIZE                     | SIZE * DRAWER_MAX_FACTOR | true          | false         | false          | true
        createFullCoverDrawer() | [drawer, Side.LEFT, -1]                || Pos.TOP_LEFT    | SIZE * DRAWER_MAX_FACTOR | SIZE                     | false         | true          | true           | false
        new VBox()              | [drawer, Side.TOP, PERCENTAGE_HALF]    || Pos.TOP_LEFT    | SIZE                     | SIZE / 2                 | true          | false         | false          | true
        new VBox()              | [drawer, Side.RIGHT, PERCENTAGE_HALF]  || Pos.TOP_RIGHT   | SIZE / 2                 | SIZE                     | false         | true          | true           | false
        new VBox()              | [drawer, Side.BOTTOM, PERCENTAGE_HALF] || Pos.BOTTOM_LEFT | SIZE                     | SIZE / 2                 | true          | false         | false          | true
        new VBox()              | [drawer, Side.LEFT, PERCENTAGE_HALF]   || Pos.TOP_LEFT    | SIZE / 2                 | SIZE                     | false         | true          | true           | false

        showDrawerCall = "showDrawer" + arguments.toString().replace('[','(').replace(']',')')
    }

    /**
     * Creates a drawer that contains elements that are the same size as the width of the workbench itself.
     * This is to test if the drawer's size is limited to 90% of the workbench width, so it is still possible to click the glasspane
     */
    def createFullCoverDrawer() {
        VBox drawer = new VBox()
        VBox drawer2 = new VBox()
        drawer2.setMinSize(SIZE, SIZE)
        drawer.getChildren().add(drawer2)
        return drawer
    }

    /**
     * Internal method to create mocks for {@link WorkbenchModule}.
     *
     * @param displayNode node to be displayed in the mock
     * @param destroy what the call for {@link WorkbenchModule#destroy()} should return
     * @param toString what {@link WorkbenchModule#toString()} should return
     * @return the mock
     */
    def createMockModule(Node displayNode, Node icon, boolean destroy, String toString) {
        WorkbenchModule mockModule = Mock(WorkbenchModule.class)
        mockModule.getName() >> toString
        mockModule.getIcon() >> icon
        mockModule.activate() >> displayNode
        mockModule.destroy() >> destroy
        mockModule.toString() >> toString
        return mockModule
    }

}
