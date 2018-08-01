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
class WorkbenchToolbarTest extends ApplicationTest {

  private FxRobot robot;
  private WorkbenchToolbar workbenchToolbar;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    workbenchToolbar = new WorkbenchToolbar();

    Scene scene = new Scene(workbenchToolbar, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testSettingStyleClasses() {
    assertTrue(workbenchToolbar.getStyleClass().contains("toolbar-control"));
    assertEquals(2, workbenchToolbar.getChildren().size());
    assertTrue(
        workbenchToolbar.getChildren().get(0).getStyleClass().contains("toolbar-control-left-box"));
    assertTrue(
        workbenchToolbar.getChildren().get(1).getStyleClass().contains("toolbar-control-right-box"));
  }

  @Test
  void testAddingItems() {
    int initialCapacity = 0;
    assertEquals(initialCapacity, verifyChildren(0));
    assertEquals(initialCapacity, verifyChildren(1));

    robot.interact(() -> {
      // Adding left a new control
      workbenchToolbar.getToolbarControlsLeft().add(new Label("content"));

      assertEquals(initialCapacity + 1, verifyChildren(0));
      assertEquals(initialCapacity, verifyChildren(1));

      // Adding right a new control
      workbenchToolbar.getToolbarControlsRight().add(new Label("content"));

      assertEquals(initialCapacity + 1, verifyChildren(0));
      assertEquals(initialCapacity + 1, verifyChildren(1));

      // Removing left the first control
      workbenchToolbar.getToolbarControlsLeft().remove(0);

      assertEquals(initialCapacity, verifyChildren(0));
      assertEquals(initialCapacity + 1, verifyChildren(1));
    });
  }

  @Test
  void testEmtyBinding() {
    assertTrue(workbenchToolbar.isEmpty());

    robot.interact(() -> {
      workbenchToolbar.getToolbarControlsLeft().add(new Label("content"));
      assertFalse(workbenchToolbar.isEmpty());

      workbenchToolbar.getToolbarControlsRight().add(new Label("content"));
      assertFalse(workbenchToolbar.isEmpty());

      workbenchToolbar.getToolbarControlsLeft().clear();
      assertFalse(workbenchToolbar.isEmpty());

      workbenchToolbar.getToolbarControlsRight().clear();
      assertTrue(workbenchToolbar.isEmpty());

      workbenchToolbar.getToolbarControlsRight().add(new Label("content"));
      assertFalse(workbenchToolbar.isEmpty());

      workbenchToolbar.getToolbarControlsRight().clear();
      assertTrue(workbenchToolbar.isEmpty());
    });
  }

  /**
   * Returns the amount of children containing in the specified box.
   *
   * @param pos 0 for searching the left box, 1 for searching in the right box
   * @return the amount of children containing in the specified box.
   */
  private int verifyChildren(int pos) {
    return ((HBox) workbenchToolbar.getChildren().get(pos)).getChildren().size();
  }
}
