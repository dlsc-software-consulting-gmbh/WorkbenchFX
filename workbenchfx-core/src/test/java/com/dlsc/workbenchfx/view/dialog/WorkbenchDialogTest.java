package com.dlsc.workbenchfx.view.dialog;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.testing.MockDialogControl;
import java.util.concurrent.TimeoutException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

class WorkbenchDialogTest extends ApplicationTest {

  private static final String TITLE = "Dialog Test Title";
  private static final String MESSAGE = "Dialog Test Message";
  private static final ButtonType[] BUTTON_TYPES = new ButtonType[]{ButtonType.PREVIOUS, ButtonType.NEXT};
  private Label CONTENT;
  private static final WorkbenchDialog.Type TYPE = WorkbenchDialog.Type.INFORMATION;
  private WorkbenchDialog dialog;

  private FxRobot robot;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();
    CONTENT = new Label(MESSAGE);
    Scene scene = new Scene(CONTENT, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testCtorContent() {
    robot.interact(() -> {
      dialog = WorkbenchDialog.builder(TITLE, CONTENT, TYPE).build();
      assertEquals(TITLE, dialog.getTitle());
      assertEquals(CONTENT, dialog.getContent());
      assertEquals(TYPE, dialog.getType());
      assertEquals(TYPE.name().toLowerCase(), dialog.getStyleClass().get(0));
    });
  }

  @Test
  void testCtorMessage() {
    robot.interact(() -> {
      dialog = WorkbenchDialog.builder(TITLE, MESSAGE, TYPE).build();
      assertEquals(TITLE, dialog.getTitle());
      assertTrue(dialog.getContent() instanceof DialogMessageContent);
      assertEquals(TYPE, dialog.getType());
      assertEquals(TYPE.name().toLowerCase(), dialog.getStyleClass().get(0));
    });
  }

  @Test
  void testCtorButtonTypesMessage() {
    robot.interact(() -> {
      dialog = WorkbenchDialog.builder(TITLE, MESSAGE, BUTTON_TYPES).build();
      assertEquals(TITLE, dialog.getTitle());
      assertTrue(dialog.getContent() instanceof DialogMessageContent);
      assertEquals(null, dialog.getType());
      assertEquals(0, dialog.getStyleClass().size());
      assertEquals(BUTTON_TYPES.length, dialog.getButtonTypes().size());
      assertArrayEquals(BUTTON_TYPES, dialog.getButtonTypes().toArray());
    });
  }

  @Test
  void testCtorButtonTypesContent() {
    robot.interact(() -> {
      dialog = WorkbenchDialog.builder(TITLE, CONTENT, BUTTON_TYPES).build();
      assertEquals(TITLE, dialog.getTitle());
      assertEquals(CONTENT, dialog.getContent());
      assertEquals(null, dialog.getType());
      assertEquals(0, dialog.getStyleClass().size());
      assertEquals(BUTTON_TYPES.length, dialog.getButtonTypes().size());
      assertArrayEquals(BUTTON_TYPES, dialog.getButtonTypes().toArray());
    });
  }

}
