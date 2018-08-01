package com.dlsc.workbenchfx.view.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
    assertTrue(
        toolbarControl.getChildren().get(0).getStyleClass().contains("toolbar-control-left-box"));
    assertTrue(
        toolbarControl.getChildren().get(1).getStyleClass().contains("toolbar-control-right-box"));
  }

  @Test
  void testAddingItems() {
    int initialCapacity = 0;
    assertEquals(initialCapacity, verifyChildren(0));
    assertEquals(initialCapacity, verifyChildren(1));

    robot.interact(() -> {
      // Adding left a new control
      toolbarControl.getToolbarControlsLeft().add(new Label("content"));

      assertEquals(initialCapacity + 1, verifyChildren(0));
      assertEquals(initialCapacity, verifyChildren(1));

      // Adding right a new control
      toolbarControl.getToolbarControlsRight().add(new Label("content"));

      assertEquals(initialCapacity + 1, verifyChildren(0));
      assertEquals(initialCapacity + 1, verifyChildren(1));

      // Removing left the first control
      toolbarControl.getToolbarControlsLeft().remove(0);

      assertEquals(initialCapacity, verifyChildren(0));
      assertEquals(initialCapacity + 1, verifyChildren(1));
    });
  }

  @Test
  void testEmtyBinding() {
    assertTrue(toolbarControl.isEmpty());

    robot.interact(() -> {
      toolbarControl.getToolbarControlsLeft().add(new Label("content"));
      assertFalse(toolbarControl.isEmpty());

      toolbarControl.getToolbarControlsRight().add(new Label("content"));
      assertFalse(toolbarControl.isEmpty());

      toolbarControl.getToolbarControlsLeft().clear();
      assertFalse(toolbarControl.isEmpty());

      toolbarControl.getToolbarControlsRight().clear();
      assertTrue(toolbarControl.isEmpty());

      toolbarControl.getToolbarControlsRight().add(new Label("content"));
      assertFalse(toolbarControl.isEmpty());

      toolbarControl.getToolbarControlsRight().clear();
      assertTrue(toolbarControl.isEmpty());
    });
  }

  /**
   * Returns the amount of children containing in the specified box.
   *
   * @param pos 0 for searching the left box, 1 for searching in the right box
   * @return the amount of children containing in the specified box.
   */
  private int verifyChildren(int pos) {
    return ((HBox) toolbarControl.getChildren().get(pos)).getChildren().size();
  }
}
