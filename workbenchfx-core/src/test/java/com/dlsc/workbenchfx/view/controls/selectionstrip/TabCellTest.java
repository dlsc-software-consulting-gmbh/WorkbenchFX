package com.dlsc.workbenchfx.view.controls.selectionstrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
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
  private final String firstChild = "first-child";
  private ObservableList<WorkbenchModule> mockList;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();
    mockBench = mock(Workbench.class);
    mockModule = mock(WorkbenchModule.class);
    mockStrip = mock(SelectionStrip.class);
    mockFactory = mock(Callback.class);
    mockTab = mock(Tab.class);
    mockProperty = mock(ObjectProperty.class);
    mockList = mock(ObservableList.class);

    tabCell = new TabCell();

    when(mockStrip.getSelectedItem()).thenReturn(mockModule);
    when(mockStrip.selectedItemProperty()).thenReturn(mockProperty);
    when(mockStrip.getItems()).thenReturn(mockList);
    when(mockList.get(0)).thenReturn(mockModule);
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
    assertTrue(tabCell.getStyleClass().contains(firstChild));

    verify(mockStrip, times(2)).getSelectedItem();
    verify(mockStrip).selectedItemProperty();
    verify(mockModule).getWorkbench();
    verify(mockBench).getTabFactory();
    verify(mockFactory).call(mockBench);
    verify(mockTab).setModule(mockModule);
    verify(mockProperty).addListener((WeakInvalidationListener) any());
    verify(mockStrip).getItems();
    verify(mockList).get(0);
  }

  @Test
  void testSettingItemNullWithSelectionStripNotNull() {
    robot.interact(() -> {
      tabCell.setSelectionStrip(mockStrip);
      tabCell.setItem(null);
    });

    assertEquals("", tabCell.getText());
    assertNull(tabCell.getGraphic());
    assertFalse(tabCell.getStyleClass().contains(firstChild));

    verify(mockStrip).getSelectedItem();
    verify(mockStrip).selectedItemProperty();
    verify(mockProperty).addListener((WeakInvalidationListener) any());
    verifyNoMoreInteractions(mockModule, mockBench, mockFactory, mockTab, mockStrip, mockList);
  }

  @Test
  void testSettingItemNullWithSelectionStripNull() {
    robot.interact(() -> {
      tabCell.setSelectionStrip(null);
      tabCell.setItem(null);
    });

    assertEquals("", tabCell.getText());
    assertNull(tabCell.getGraphic());
    assertFalse(tabCell.getStyleClass().contains(firstChild));

    verifyNoMoreInteractions(
        mockStrip, mockModule, mockBench, mockFactory, mockTab, mockProperty, mockStrip, mockList
    );
  }
}
