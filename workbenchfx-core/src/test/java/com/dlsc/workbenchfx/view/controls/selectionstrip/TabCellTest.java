package com.dlsc.workbenchfx.view.controls.selectionstrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

public class TabCellTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private WorkbenchModule mockModule;
  private SelectionStrip<WorkbenchModule> mockStrip;
  private Callback<Workbench, Tab> mockFactory;
  private Tab mockTab;
  private ObjectProperty<WorkbenchModule> mockProperty;

  private TabCell tabCell;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();
    mockBench = mock(Workbench.class);
    mockModule = mock(WorkbenchModule.class);
    mockStrip = mock(SelectionStrip.class);
    mockFactory = mock(Callback.class);
    mockTab = mock(Tab.class);
    mockProperty = mock(ObjectProperty.class);

    tabCell = new TabCell();

    when(mockStrip.getSelectedItem()).thenReturn(mockModule);
    when(mockStrip.selectedItemProperty()).thenReturn(mockProperty);
    when(mockModule.getWorkbench()).thenReturn(mockBench);
    when(mockBench.getTabFactory()).thenReturn(mockFactory);
    when(mockFactory.call(mockBench)).thenReturn(mockTab);

    Scene scene = new Scene(tabCell, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testSettingItemNotNullWithSelectionStripNotNull() {
    robot.interact(() -> {
      tabCell.setSelectionStrip(mockStrip);
      tabCell.setItem(mockModule);
    });

    assertEquals("", tabCell.getText());
    assertEquals(mockTab, tabCell.getGraphic());

    verify(mockStrip, times(2)).getSelectedItem();
    verify(mockStrip).selectedItemProperty();
    verify(mockModule).getWorkbench();
    verify(mockBench).getTabFactory();
    verify(mockFactory).call(mockBench);
    verify(mockTab).setModule(mockModule);
    verify(mockProperty).addListener((WeakInvalidationListener) any());
  }

//  @Test
//  void testSettingItemNotNullWithSelectionStripNull() {
//    robot.interact(() -> {
//      tabCell.setSelectionStrip(null);
//      try {
//        tabCell.setItem(mockModule);
//        fail("Should throw a NullpointerException");
//      } catch (Exception e) {
//        assertTrue(e instanceof NullPointerException);
//      }
//    });
//
//    assertEquals("", tabCell.getText());
//    assertEquals(mockTab, tabCell.getGraphic());
//
//    verify(mockStrip, never()).getSelectedItem();
//    verify(mockStrip, never()).selectedItemProperty();
//    verify(mockModule).getWorkbench();
//    verify(mockBench).getTabFactory();
//    verify(mockFactory).call(mockBench);
//    verify(mockTab).setModule(mockModule);
//    verify(mockProperty, never()).addListener((WeakInvalidationListener) any());
//  }

  @Test
  void testSettingItemNullWithSelectionStripNotNull() {
    robot.interact(() -> {
      tabCell.setSelectionStrip(mockStrip);
      tabCell.setItem(null);
    });

    assertEquals("", tabCell.getText());
    assertEquals(null, tabCell.getGraphic());

    verify(mockStrip).getSelectedItem();
    verify(mockStrip).selectedItemProperty();
    verify(mockModule, never()).getWorkbench();
    verify(mockBench, never()).getTabFactory();
    verify(mockFactory, never()).call(mockBench);
    verify(mockTab, never()).setModule(mockModule);
    verify(mockProperty).addListener((WeakInvalidationListener) any());
  }

  @Test
  void testSettingItemNullWithSelectionStripNull() {
    robot.interact(() -> {
      tabCell.setSelectionStrip(null);
      tabCell.setItem(null);
    });

    assertEquals("", tabCell.getText());
    assertEquals(null, tabCell.getGraphic());

    verify(mockStrip, never()).getSelectedItem();
    verify(mockStrip, never()).selectedItemProperty();
    verify(mockModule, never()).getWorkbench();
    verify(mockBench, never()).getTabFactory();
    verify(mockFactory, never()).call(mockBench);
    verify(mockTab, never()).setModule(mockModule);
    verify(mockProperty, never()).addListener((WeakInvalidationListener) any());
  }

//  @Test
//  void testSettingItemNotNullSelectionStripNotNullWorkbenchNull() {
//    when(mockModule.getWorkbench()).thenReturn(null); // Simulating a workbench which is null
//
//    robot.interact(() -> {
//      Exception exception = null;
//
//      tabCell.setSelectionStrip(mockStrip);
//      try {
//        tabCell.setItem(mockModule);
//        fail("Should throw a NullpointerException");
//      } catch (Exception e) {
//        assertTrue(e instanceof NullPointerException);
//      }
//    });
//
//    assertEquals("", tabCell.getText());
//    assertNull(tabCell.getGraphic());
//
//    verify(mockStrip, times(2)).getSelectedItem();
//    verify(mockStrip).selectedItemProperty();
//
//    verify(mockModule).getWorkbench();
//    verify(mockBench, never()).getTabFactory();
//    verify(mockFactory, never()).call(mockBench);
//    verify(mockTab, never()).setModule(mockModule);
//    verify(mockProperty).addListener((WeakInvalidationListener) any());
//  }
}
