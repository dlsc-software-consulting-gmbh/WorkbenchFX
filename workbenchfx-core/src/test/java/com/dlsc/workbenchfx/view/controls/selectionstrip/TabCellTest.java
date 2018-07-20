package com.dlsc.workbenchfx.view.controls.selectionstrip;

import static org.mockito.Mockito.mock;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

public class TabCellTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private WorkbenchModule mockModule;
  private SelectionStrip<Workbench> mockStrip;

  private TabCell tabCell;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();
    mockBench = mock(Workbench.class);
    mockModule = mock(WorkbenchModule.class);
    mockStrip = mock(SelectionStrip.class);

    tabCell = new TabCell();

    Scene scene = new Scene(tabCell, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

}
