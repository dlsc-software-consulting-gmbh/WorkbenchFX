package com.dlsc.workbenchfx.view.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;


/**
 * Test for {@link NavigationDrawer}.
 */
class ToolbarControlTest extends ApplicationTest {

  private FxRobot robot;
  private ToolbarControl toolbarControl;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    toolbarControl = new ToolbarControl();

    Scene scene = new Scene(toolbarControl, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testSettingStyleClasses() {
    assertTrue(toolbarControl.getStyleClass().contains("toolbar-control"));
    assertEquals(2, toolbarControl.getChildren().size());
    assertTrue(toolbarControl.getChildren().get(0).getStyleClass().contains("toolbar-control-left-box"));
    assertTrue(toolbarControl.getChildren().get(1).getStyleClass().contains("toolbar-control-right-box"));
  }

  @Test
  void test() {

  }
}
