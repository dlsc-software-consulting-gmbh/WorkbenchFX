package com.dlsc.workbenchfx.view.dialog;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.WorkbenchModule;
import com.dlsc.workbenchfx.testing.MockDialogControl;
import com.dlsc.workbenchfx.testing.MockPage;
import com.dlsc.workbenchfx.testing.MockTile;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

class DialogControlTest extends ApplicationTest {
  private static final ButtonType BUTTON_TYPE = ButtonType.YES;

  private FxRobot robot;
  private Workbench mockBench;

  private WorkbenchDialog mockDialog;
  private ObservableList<ButtonType> buttonTypes;

  private DialogControl dialogControl;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    mockDialog = mock(WorkbenchDialog.class);
    buttonTypes = FXCollections.observableArrayList(BUTTON_TYPE);
    when(mockDialog.getButtonTypes()).thenReturn(buttonTypes);

    mockBench = mock(Workbench.class);
    when(mockBench.dialogProperty()).thenReturn(new SimpleObjectProperty<>(mockDialog));

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
  void hide() {
    dialogControl.hide();
    verify(mockBench).hideDialog();
  }

  @Test
  void getDialog() {
    assertEquals(mockDialog, dialogControl.getDialog());
  }
}
