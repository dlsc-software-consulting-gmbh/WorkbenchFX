package com.dlsc.workbenchfx.view.controls;

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
  void test() {

  }
}
