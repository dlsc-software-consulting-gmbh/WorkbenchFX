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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

  private CompletableFuture<ButtonType> result = new CompletableFuture<>();

  @Mock
  private EventHandler<Event> mockShownHandler;

  @Mock
  private EventHandler<Event> mockHiddenHandler;

  @Override
  public void start(Stage stage) {
    MockitoAnnotations.initMocks(this);

    robot = new FxRobot();

    mockDialog = mock(WorkbenchDialog.class);
    buttonTypes = FXCollections.observableArrayList(BUTTON_TYPE_1);
    when(mockDialog.getButtonTypes()).thenReturn(buttonTypes);

    mockBench = mock(Workbench.class);

    dialogControl = new MockDialogControl();

    // simulate call of workbench to set itself in the dialogControl
    dialogControl.setWorkbench(mockBench);
    // simulate call of WorkbenchDialog to set itself in the dialogControl
    dialogControl.setDialog(mockDialog);

    // setup mocks for listeners
    dialogControl.setOnHidden(mockHiddenHandler);
    dialogControl.setOnShown(mockShownHandler);

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
      ObservableList<Button> buttons = dialogControl.getButtons();
      assertSame(1, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), buttons.get(0).getText());
      verify(mockDialog, times(2)).getButtonTypes();

      // change ButtonTextUppercase to uppercase
      dialogControl.setButtonTextUppercase(true);
      assertSame(1, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText().toUpperCase(), buttons.get(0).getText());
      verify(mockDialog, times(3)).getButtonTypes();

      // add buttonType
      buttonTypes.add(BUTTON_TYPE_2);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText().toUpperCase(), buttons.get(0).getText());
      assertEquals(BUTTON_TYPE_2.getText().toUpperCase(), buttons.get(1).getText());
      verify(mockDialog, times(4)).getButtonTypes();

      // change ButtonTextUppercase back to lowercase
      dialogControl.setButtonTextUppercase(false);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), buttons.get(0).getText());
      assertEquals(BUTTON_TYPE_2.getText(), buttons.get(1).getText());
      verify(mockDialog, times(5)).getButtonTypes();

      // change dialog
      dialogControl.setDialog(null);
      assertSame(0, buttons.size());
      verify(mockDialog, times(6)).getButtonTypes();

      // change dialog back
      dialogControl.setDialog(mockDialog);
      assertSame(2, buttons.size());
      assertEquals(BUTTON_TYPE_1.getText(), buttons.get(0).getText());
      assertEquals(BUTTON_TYPE_2.getText(), buttons.get(1).getText());
      verify(mockDialog, times(8)).getButtonTypes();
    });
  }

  @Disabled // TODO
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

  @Disabled // TODO
  @Test
  void getDialog() {
    assertEquals(mockDialog, dialogControl.getDialog());
  }
}
