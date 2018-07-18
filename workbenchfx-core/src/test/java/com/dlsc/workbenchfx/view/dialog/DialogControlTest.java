package com.dlsc.workbenchfx.view.dialog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.testing.MockDialogControl;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

class DialogControlTest extends ApplicationTest {
  private static final ButtonType BUTTON_TYPE_1 = ButtonType.YES;
  private static final ButtonType BUTTON_TYPE_2 = ButtonType.APPLY;

  private FxRobot robot;
  private Workbench mockBench;

  private WorkbenchDialog mockDialog;
  private ObservableList<ButtonType> buttonTypes;

  private DialogControl dialogControl;
  private SimpleObjectProperty<WorkbenchDialog> dialogProperty;

  private CompletableFuture<ButtonType> result = new CompletableFuture<>();

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    mockDialog = mock(WorkbenchDialog.class);
    buttonTypes = FXCollections.observableArrayList(BUTTON_TYPE_1);
    when(mockDialog.getButtonTypes()).thenReturn(buttonTypes);
    // TODO: REFACTOR when(mockDialog.getResult()).thenReturn(result);

    mockBench = mock(Workbench.class);
    dialogProperty = new SimpleObjectProperty<>(mockDialog);
    when(mockBench.dialogProperty()).thenReturn(dialogProperty);

    dialogControl = new MockDialogControl();

    // simulate call of workbench to set itself in the dialogControl
    dialogControl.setWorkbench(mockBench);
    Scene scene = new Scene(dialogControl, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testCtor() {
    assertFalse(dialogControl.isPickOnBounds());
  }

  @Test
  void testListeners() {
    robot.interact(() -> {
      // initially
      ObservableList<Node> buttons = dialogControl.getButtons();
      assertSame(1, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      verify(mockDialog, times(2)).getButtonTypes();

      // change ButtonTextUppercase to uppercase
      dialogControl.setButtonTextUppercase(true);
      assertSame(1, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText().toUpperCase(), ((Button)buttons.get(0)).getText());
      verify(mockDialog, times(3)).getButtonTypes();

      // add buttonType
      buttonTypes.add(BUTTON_TYPE_2);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText().toUpperCase(), ((Button)buttons.get(0)).getText());
      assertEquals(BUTTON_TYPE_2.getText().toUpperCase(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(4)).getButtonTypes();

      // change ButtonTextUppercase back to lowercase
      dialogControl.setButtonTextUppercase(false);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      assertEquals(BUTTON_TYPE_2.getText(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(5)).getButtonTypes();

      // change workbench
      dialogControl.setWorkbench(null);
      assertSame(0, buttons.size());
      verify(mockDialog, times(7)).getButtonTypes();

      // change workbench back
      dialogControl.setWorkbench(mockBench);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      assertEquals(BUTTON_TYPE_2.getText(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(9)).getButtonTypes();

      // change dialog
      dialogProperty.set(null);
      assertSame(0, buttons.size());
      verify(mockDialog, times(10)).getButtonTypes();

      // change dialog back
      dialogProperty.set(mockDialog);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      assertEquals(BUTTON_TYPE_2.getText(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(12)).getButtonTypes();
    });
  }

  @Test
  void testButtonActions() {
    robot.interact(() -> {
      // initially
      ObservableList<Node> buttons = dialogControl.getButtons();
      assertSame(1, buttons.size());
      Button button = ((Button) buttons.get(0));
      assertEquals(BUTTON_TYPE_1.getText(), button.getText());

      // verify result before firing event
      assertFalse(result.isDone());

      // fire event (simulate click on button) causing setOnAction to get triggered
      button.fire();
      assertTrue(result.isDone());
      try {
        assertEquals(BUTTON_TYPE_1, result.get());
      } catch (InterruptedException | ExecutionException e) {
        fail("Could not get result of dialog!");
      }
      // TODO: verify(mockBench).hideDialog();
    });
  }

  @Test
  void hide() {
    dialogControl.hide();
    // TODO: verify(mockBench).hideDialog();
  }

  @Test
  void getDialog() {
    assertEquals(mockDialog, dialogControl.getDialog());
  }
}
