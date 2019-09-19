package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.testing.MockPage;
import com.dlsc.workbenchfx.testing.MockTab;
import com.dlsc.workbenchfx.testing.MockTile;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.EmptyNodeQueryException;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Workbench} in case only a single module is used
 */
public class SingleModuleWorkbenchTest extends ApplicationTest {
    Workbench workbench;
    private FxRobot robot;

    @Override
    public void start(Stage stage) {
        MockitoAnnotations.initMocks(this);
        robot = new FxRobot();

        Node moduleNode = new Label("Module Content");
        WorkbenchModule mockModule = createMockModule(
                moduleNode, null, true, "Module 1", workbench,
                FXCollections.observableArrayList(), FXCollections.observableArrayList()
        );

        workbench = Workbench.builder(
                mockModule)
                .tabFactory(MockTab::new)
                .tileFactory(MockTile::new)
                .pageFactory(MockPage::new)
                .build();

        Scene scene = new Scene(workbench, 100, 100);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void moduleAutomaticallyOpened(){
        assertEquals(workbench.getActiveModule(), workbench.getModules().get(0));
    }

    @Test
    void tabBarInvisible(){
        // the bottom-box should not be in the scene graph and therefore the robot should throw an exception
        assertThrows(EmptyNodeQueryException.class, () -> {
            robot.lookup("#bottom-box").queryAs(HBox.class);
        });
    }
}
