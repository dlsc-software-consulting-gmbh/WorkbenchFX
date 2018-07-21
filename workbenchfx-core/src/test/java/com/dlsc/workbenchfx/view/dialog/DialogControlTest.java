package com.dlsc.workbenchfx.view.dialog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.testing.MockDialogControl;
import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
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

  @Mock
  private EventHandler<Event> mockShownHandler;

  @Mock
  private EventHandler<Event> mockHiddenHandler;

  @Mock
  private EventHandler<Event> mockShownHandler2;

  @Mock
  private EventHandler<Event> mockHiddenHandler2;

  @Mock
  private Consumer<ButtonType> mockOnResult;

  private Stage stage;
  private DialogControl dialogControl2;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    MockitoAnnotations.initMocks(this);

    robot = new FxRobot();

    mockDialog = mock(WorkbenchDialog.class);
    buttonTypes = FXCollections.observableArrayList(BUTTON_TYPE_1);
    when(mockDialog.getButtonTypes()).thenReturn(buttonTypes);
    when(mockDialog.getOnResult()).thenReturn(mockOnResult);

    mockBench = mock(Workbench.class);

    dialogControl = new MockDialogControl();

    // simulate call of workbench to set itself in the dialogControl
    dialogControl.setWorkbench(mockBench);
    // simulate call of WorkbenchDialog to set itself in the dialogControl
    dialogControl.setDialog(mockDialog);

    // setup mocks for listeners
    dialogControl.setOnHidden(mockHiddenHandler);
    dialogControl.setOnShown(mockShownHandler);

    // setup second dialog control that isn't showing, to test behavior of skin listeners
    dialogControl2 = new MockDialogControl();
    dialogControl2.setDialog(mockDialog);
    dialogControl2.setOnShown(mockShownHandler2);
    dialogControl2.setOnHidden(mockHiddenHandler2);

    Scene scene = new Scene(dialogControl, 100, 100);
    this.stage.setScene(scene);
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

      verify(mockShownHandler).handle(any());
    });
  }

  @Test
  void testOnShownHiddenListeners() {
    robot.interact(() -> {
      // Make sure dialogControl2's skin hasn't been initialized yet
      assertNull(dialogControl2.getSkin());
      assertFalse(dialogControl2.isShowing());

      // when setting workbench, it shouldn't trigger an onShown event
      dialogControl2.setWorkbench(mockBench);
      assertFalse(dialogControl2.isShowing());
      verify(mockShownHandler2, never()).handle(any());

      // when setting workbench to null, it shouldn't trigger an onHidden event
      dialogControl2.setWorkbench(null);
      verify(mockHiddenHandler2, never()).handle(any());
      assertFalse(dialogControl2.isShowing());

      // **** Simulate Dialog getting shown ****
      // simulate call from Workbench#showDialog()
      dialogControl2.setWorkbench(mockBench);
      // initialize skin
      stage.setScene(new Scene(dialogControl2, 100, 100));
      assertNotNull(dialogControl2.getSkin());

      // shownHandler should've been triggered
      assertTrue(dialogControl2.isShowing());
      verify(mockShownHandler2).handle(any());
      verify(mockHiddenHandler2, never()).handle(any());

      // **** Simulate Dialog getting hidden ****
      // simulate call from Workbench#hideDialog()
      dialogControl2.setWorkbench(null);

      // hiddenHandler should've been triggered
      assertFalse(dialogControl2.isShowing());
      verify(mockHiddenHandler2).handle(any());

    });
  }
  
  @Test
  void testButtonActions() {
    robot.interact(() -> {
      // initially
      ObservableList<Button> buttons = dialogControl.getButtons();
      assertSame(1, buttons.size());
      Button button = buttons.get(0);
      assertEquals(BUTTON_TYPE_1.getText(), button.getText());

      // verify result before firing event
      verify(mockOnResult, never()).accept(any());

      // fire event (simulate click on button) causing setOnAction to get triggered
      button.fire();
      verify(mockOnResult).accept(BUTTON_TYPE_1);
      verify(mockBench).hideDialog(mockDialog);
    });
  }

  @Test
  void hide() {
    dialogControl.hide();
    verify(mockBench).hideDialog(mockDialog);
  }

  @Test
  void getDialog() {
    assertEquals(mockDialog, dialogControl.getDialog());
  }

  @Test
  void getButton() {
    // when asking for a button type
    Optional<Button> button = dialogControl.getButton(BUTTON_TYPE_1);

    // returns its button in an Optional
    assertNotEquals(Optional.empty(), button);
    assertEquals(dialogControl.getButtons().get(0), button.get());

    // if the buttonType doesn't exist
    button = dialogControl.getButton(ButtonType.CANCEL);

    // return empty optional
    assertEquals(Optional.empty(), button);

    // if there are no buttonTypes
    buttonTypes.clear();
    button = dialogControl.getButton(BUTTON_TYPE_1);

    // return empty optional
    assertEquals(Optional.empty(), button);
  }
}
