package com.dlsc.workbenchfx.view.controls.module;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.testing.MockPage;
import com.dlsc.workbenchfx.testing.MockTab;
import com.dlsc.workbenchfx.testing.MockTile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Created by François Martin on 05.06.18.
 */
class PageTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private static final int SIZE = 10;
  Module[] mockModules = new Module[SIZE];
  Node[] moduleNodes = new Node[SIZE];

  int modulesPerPage;

  private MockPage page0;
  private MockPage page1;
  private ObservableSet<Tile> tiles0;
  private ObservableSet<Tile> tiles1;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    mockBench = mock(Workbench.class);

    for (int i = 0; i < moduleNodes.length; i++) {
      moduleNodes[i] = new Label("Module Content");
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = createMockModule(moduleNodes[i], true, "Module " + i);
      when(mockBench.getTile(mockModules[i])).thenReturn(new MockTile(mockBench));
    }

    when(mockBench.getModules()).thenReturn(FXCollections.observableArrayList(mockModules));

    setModulesPerPage(9);

    page0 = new MockPage(mockBench);
    page0.setPageIndex(0);
    tiles0 = page0.getTiles();

    page1 = new MockPage(mockBench);
    page1.setPageIndex(1);
    tiles1 = page1.getTiles();

    Scene scene = new Scene(new HBox(page0, page1), 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  void setModulesPerPage(int amount){
    modulesPerPage = amount;
    when(mockBench.getModulesPerPage()).thenReturn(modulesPerPage);
  }

  @Test
  void updateTiles() {
    robot.interact(() -> {
      // verify initial amount of tiles is correct
      assertEquals(modulesPerPage, tiles0.size());
      assertEquals(SIZE - modulesPerPage, tiles1.size());
    });
  }
}
