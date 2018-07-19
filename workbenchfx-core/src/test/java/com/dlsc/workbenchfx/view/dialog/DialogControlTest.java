package com.dlsc.workbenchfx.view.dialog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import javafx.scene.Scene;
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
    // TODO: when(mockBench.dialogProperty()).thenReturn(dialogProperty);

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
      // TODO: ObservableList<Node> buttons = dialogControl.getButtons();
      // TODO: assertSame(1, buttons.size());
      // TODO: assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      verify(mockDialog, times(2)).getButtonTypes();

      // change ButtonTextUppercase to uppercase
      dialogControl.setButtonTextUppercase(true);
      // TODO: assertSame(1, buttons.size());
      // TODO:
      // assertEquals(BUTTON_TYPE_1.getText().toUpperCase(), ((Button)buttons.get(0)).getText());
      verify(mockDialog, times(3)).getButtonTypes();

      // add buttonType
      buttonTypes.add(BUTTON_TYPE_2);
      // TODO: assertSame(2, buttons.size());
      // TODO:
      // assertEquals(BUTTON_TYPE_1.getText().toUpperCase(), ((Button)buttons.get(0)).getText());
      // TODO:
      // assertEquals(BUTTON_TYPE_2.getText().toUpperCase(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(4)).getButtonTypes();

      // change ButtonTextUppercase back to lowercase
      dialogControl.setButtonTextUppercase(false);
      // TODO: assertSame(2, buttons.size());
      // TODO: assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      // TODO: assertEquals(BUTTON_TYPE_2.getText(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(5)).getButtonTypes();

      // change workbench
      dialogControl.setWorkbench(null);
      // TODO: assertSame(0, buttons.size());
      verify(mockDialog, times(7)).getButtonTypes();

      // change workbench back
      dialogControl.setWorkbench(mockBench);
      // TODO: assertSame(2, buttons.size());
      // TODO: assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      // TODO: assertEquals(BUTTON_TYPE_2.getText(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(9)).getButtonTypes();

      // change dialog
      dialogProperty.set(null);
      // TODO: assertSame(0, buttons.size());
      verify(mockDialog, times(10)).getButtonTypes();

      // change dialog back
      dialogProperty.set(mockDialog);
      // TODO: assertSame(2, buttons.size());
      // TODO: assertEquals(BUTTON_TYPE_1.getText(), ((Button)buttons.get(0)).getText());
      // TODO: assertEquals(BUTTON_TYPE_2.getText(), ((Button)buttons.get(1)).getText());
      verify(mockDialog, times(12)).getButtonTypes();
    });
  }

  @Test
  void testButtonActions() {
    robot.interact(() -> {
      // initially
      // TODO: ObservableList<Node> buttons = dialogControl.getButtons();
      // TODO: assertSame(1, buttons.size());
      // TODO: Button button = ((Button) buttons.get(0));
      // TODO: assertEquals(BUTTON_TYPE_1.getText(), button.getText());

      // verify result before firing event
      assertFalse(result.isDone());

      // fire event (simulate click on button) causing setOnAction to get triggered
      // TODO: button.fire();
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
