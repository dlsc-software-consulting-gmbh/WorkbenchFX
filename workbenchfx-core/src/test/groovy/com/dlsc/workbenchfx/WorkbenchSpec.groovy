package com.dlsc.workbenchfx

import com.dlsc.workbenchfx.model.WorkbenchModule
import com.dlsc.workbenchfx.testing.*
import com.dlsc.workbenchfx.view.controls.Dropdown
import com.dlsc.workbenchfx.view.controls.GlassPane
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import org.spockframework.util.ExceptionUtil
import org.testfx.api.FxRobot
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Unroll

import static com.dlsc.workbenchfx.model.WorkbenchDialog.Type

@Unroll
class WorkbenchSpec extends ApplicationSpec {

    private static final int SIZE = 3

    private static final int FIRST_INDEX = 0
    private static final int SECOND_INDEX = 1
    private static final int LAST_INDEX = SIZE - 1

    private static final String TITLE = "Title of a Dialog"
    private static final String MESSAGE = "Message of a Dialog"
    private static final String DETAILS = "Details of a Dialog"
    private static final Exception EXCEPTION = new Exception()

    Workbench workbench

    WorkbenchModule[] mockModules = new WorkbenchModule[SIZE]
    Node[] moduleNodes = new Node[SIZE]

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

    private FxRobot robot

    // Dropdown items
    private String dropdownText
    private FontAwesomeIconView dropdownIconView
    private ImageView dropdownImageView
    private MenuItem dropdownMenuItem
    private Dropdown dropdownLeft
    private Dropdown dropdownRight

    private MockNavigationDrawer navigationDrawer
    private MockDialogControl dialogControl

    @Override
    void start(Stage stage) {

        robot = new FxRobot()

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
                .dialogControl(dialogControl)
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

        Scene scene = new Scene(workbench, 100, 100)
        stage.setScene(scene)
        stage.show()
    }

    def "Test #methodName(#arguments)"() {
        given:
        def result
        robot.interact {
            result = workbench."$methodName"(arguments)
        }
        def currentDialog = workbench.getDialog()

        expect:
        type == currentDialog.getType()
        TITLE == currentDialog.getTitle()
        MESSAGE == ((Label) currentDialog.getContent()).getText()
        exception == currentDialog.getException()
        details == currentDialog.getDetails()
        result == currentDialog.getResult()

        where:
        methodName               | arguments                   || type              | exception | details
        "showErrorDialog"        | [TITLE, MESSAGE]            || Type.ERROR        | null      | ""
        "showErrorDialog"        | [TITLE, MESSAGE, DETAILS]   || Type.ERROR        | null      | DETAILS
        "showErrorDialog"        | [TITLE, MESSAGE, EXCEPTION] || Type.ERROR        | EXCEPTION | ExceptionUtil.printStackTrace(exception)
        "showWarningDialog"      | [TITLE, MESSAGE]            || Type.WARNING      | null      | ""
        "showConfirmationDialog" | [TITLE, MESSAGE]            || Type.CONFIRMATION | null      | ""
        "showInformationDialog"  | [TITLE, MESSAGE]            || Type.INFORMATION  | null      | ""
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
