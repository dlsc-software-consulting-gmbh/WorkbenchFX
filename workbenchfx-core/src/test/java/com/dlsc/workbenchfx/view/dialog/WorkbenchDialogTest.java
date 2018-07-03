package com.dlsc.workbenchfx.view.dialog;

import static com.dlsc.workbenchfx.view.dialog.WorkbenchDialog.Type;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

class WorkbenchDialogTest extends ApplicationTest {

  private static final String TITLE = "Dialog Test Title";
  private static final String MESSAGE = "Dialog Test Message";
  private static final ButtonType[] BUTTON_TYPES =
      new ButtonType[] {ButtonType.PREVIOUS, ButtonType.NEXT};
  private Label content;
  private static final WorkbenchDialog.Type TYPE = WorkbenchDialog.Type.INFORMATION;
  private WorkbenchDialog dialog;

  private FxRobot robot;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();
    content = new Label(MESSAGE);
    Scene scene = new Scene(content, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testCtorContent() {
    robot.interact(() -> {
      dialog = WorkbenchDialog.builder(TITLE, content, TYPE).build();
      assertEquals(TITLE, dialog.getTitle());
      assertEquals(content, dialog.getContent());
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
      dialog = WorkbenchDialog.builder(TITLE, content, BUTTON_TYPES).build();
      assertEquals(TITLE, dialog.getTitle());
      assertEquals(content, dialog.getContent());
      assertEquals(null, dialog.getType());
      assertEquals(0, dialog.getStyleClass().size());
      assertEquals(BUTTON_TYPES.length, dialog.getButtonTypes().size());
      assertArrayEquals(BUTTON_TYPES, dialog.getButtonTypes().toArray());
    });
  }

  @Test
  void testCtorOptional() {
    // test all optional parameters available
    // TODO
  }

  @Test
  void testInitType() {
    robot.interact(() -> {
      Type type = Type.INPUT;
      ButtonType[] buttonTypes = new ButtonType[] {ButtonType.OK, ButtonType.CANCEL};
      dialog = WorkbenchDialog.builder(TITLE, content, type).build();
      assertEquals(type, dialog.getType());
      assertEquals(buttonTypes.length, dialog.getButtonTypes().size());
      assertArrayEquals(buttonTypes, dialog.getButtonTypes().toArray());

      type = Type.INFORMATION;
      buttonTypes = new ButtonType[] {ButtonType.OK};
      dialog = WorkbenchDialog.builder(TITLE, content, type).build();
      assertEquals(type, dialog.getType());
      assertEquals(buttonTypes.length, dialog.getButtonTypes().size());
      assertArrayEquals(buttonTypes, dialog.getButtonTypes().toArray());

      type = Type.ERROR;
      buttonTypes = new ButtonType[] {ButtonType.CLOSE};
      dialog = WorkbenchDialog.builder(TITLE, content, type).build();
      assertEquals(type, dialog.getType());
      assertEquals(buttonTypes.length, dialog.getButtonTypes().size());
      assertArrayEquals(buttonTypes, dialog.getButtonTypes().toArray());

      type = Type.WARNING;
      buttonTypes = new ButtonType[] {ButtonType.OK, ButtonType.CANCEL};
      dialog = WorkbenchDialog.builder(TITLE, content, type).build();
      assertEquals(type, dialog.getType());
      assertEquals(buttonTypes.length, dialog.getButtonTypes().size());
      assertArrayEquals(buttonTypes, dialog.getButtonTypes().toArray());

      type = Type.CONFIRMATION;
      buttonTypes = new ButtonType[] {ButtonType.YES, ButtonType.NO};
      dialog = WorkbenchDialog.builder(TITLE, content, type).build();
      assertEquals(type, dialog.getType());
      assertEquals(buttonTypes.length, dialog.getButtonTypes().size());
      assertArrayEquals(buttonTypes, dialog.getButtonTypes().toArray());

      // test default case
      final Type mockType = mock(Type.class);
      when(mockType.name()).thenReturn("THIS IS NOT POSSIBLE");
      WorkbenchDialog build = WorkbenchDialog.builder(TITLE, content, mockType).build();
      System.out.println(build.getButtonTypes());
      // TODO: why no exception thrown?
      /*assertThrows(UnsupportedOperationException.class,
          () -> WorkbenchDialog.builder(TITLE, content, mockType).build());*/
    });
  }
}
