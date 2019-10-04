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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Workbench} in case only a single module is used
 */
@Tag("fast")
public class SingleModuleWorkbenchTest extends ApplicationTest {
    Workbench workbench;
    WorkbenchModule mockModule;
    private FxRobot robot;

    @Override
    public void start(Stage stage) {
        MockitoAnnotations.initMocks(this);
        robot = new FxRobot();

        Node moduleNode = new Label("Module Content");
        mockModule = createMockModule(
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
      assertSame(mockModule, workbench.getActiveModule());
    }

    @Test
    void tabBarInvisible(){
        // the bottom-box should not be visible
        assertFalse(robot.lookup("#bottom-box").queryAs(HBox.class).isVisible());
    }

    @Test
    void tabBarVisibilityChanges(){
      HBox bottomBox = robot.lookup("#bottom-box").queryAs(HBox.class);

      WorkbenchModule anotherModule = createMockModule(
              new Label(), null, true, "Module 2", workbench,
              FXCollections.observableArrayList(), FXCollections.observableArrayList());


      robot.interact(() -> {
        // add a second module to the workbench, the bottom-box should be visible now
        workbench.getModules().add(anotherModule);
        assertTrue(bottomBox.isVisible());

        workbench.getModules().remove(anotherModule);
        assertFalse(bottomBox.isVisible());
      });
    }
}
