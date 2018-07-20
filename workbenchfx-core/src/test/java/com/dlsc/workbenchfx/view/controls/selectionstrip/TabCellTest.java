package com.dlsc.workbenchfx.view.controls.selectionstrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
  void testSettingItem() {
    robot.interact(() -> {
      tabCell.setSelectionStrip(mockStrip);
      tabCell.setItem(mockModule);
    });

    assertEquals("", tabCell.getText());
    assertEquals(mockTab, tabCell.getGraphic());

    verify(mockStrip, times(2)).getSelectedItem();
    verify(mockStrip, times(1)).selectedItemProperty();
    verify(mockModule, times(1)).getWorkbench();
    verify(mockBench, times(1)).getTabFactory();
    verify(mockFactory, times(1)).call(mockBench);
    verify(mockTab, times(1)).setModule(mockModule);
    verify(mockProperty, times(1)).addListener((WeakInvalidationListener) any());
  }
}
