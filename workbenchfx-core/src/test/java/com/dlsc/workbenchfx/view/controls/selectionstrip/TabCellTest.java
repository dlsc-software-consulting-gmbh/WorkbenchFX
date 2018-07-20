package com.dlsc.workbenchfx.view.controls.selectionstrip;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

public class TabCellTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private WorkbenchModule mockModule;
  private SelectionStrip<Workbench> mockStrip;
  private Callback<Workbench, Tab> mockFactory;
  private Tab mockTab;

  private TabCell tabCell;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();
    mockBench = mock(Workbench.class);
    mockModule = mock(WorkbenchModule.class);
    mockStrip = mock(SelectionStrip.class);
    mockFactory = mock(Callback.class);
    mockTab = mock(Tab.class);

    tabCell = new TabCell();

    when(mockModule.getWorkbench()).thenReturn(mockBench);
    when(mockBench.getTabFactory()).thenReturn(mockFactory);
    when(mockFactory.call(mockBench)).thenReturn(mockTab);

    Scene scene = new Scene(tabCell, 100, 100);
    stage.setScene(scene);
    stage.show();
  }



}
