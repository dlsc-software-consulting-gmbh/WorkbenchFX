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
import java.util.LinkedHashSet;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

  IntegerProperty modulesPerPage;

  private MockPage page0;
  private MockPage page1;
  private ObservableList<Tile> tiles0;
  private ObservableList<Tile> tiles1;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    mockBench = mock(Workbench.class);

    for (int i = 0; i < moduleNodes.length; i++) {
      moduleNodes[i] = new Label("Module Content");
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = createMockModule(moduleNodes[i], true, "Module " + i);
      MockTile mockTile = new MockTile(mockBench);
      mockTile.setModule(mockModules[i]);
      when(mockBench.getTile(mockModules[i])).thenReturn(mockTile);
    }

    when(mockBench.getModules()).thenReturn(FXCollections.observableArrayList(mockModules));

    modulesPerPage = new SimpleIntegerProperty();
    when(mockBench.modulesPerPageProperty()).thenReturn(modulesPerPage);
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

  void setModulesPerPage(int amount) {
    robot.interact(() -> {
      when(mockBench.getModulesPerPage()).thenReturn(amount);
      modulesPerPage.set(amount);
    });
  }

  @Test
  void updateTilesInitial() {
    robot.interact(() -> {
      // verify initial amount of tiles is correct
      assertEquals(modulesPerPage.get(), tiles0.size());
      assertEquals(SIZE - modulesPerPage.get(), tiles1.size());
      assertEquals(mockModules[0],tiles0.get(0).getModule());
      assertEquals(mockModules[SIZE-1],tiles1.get(0).getModule());
    });
  }

  @Test
  void updateTilesPageIndexChanged() {
    robot.interact(() -> {
      // tiles get updated when page index gets changed
      page0.setPageIndex(1);
      page1.setPageIndex(0);

      assertEquals(SIZE - modulesPerPage.get(), tiles0.size());
      assertEquals(modulesPerPage.get(), tiles1.size());
      assertEquals(mockModules[0],tiles1.get(0).getModule());
      assertEquals(mockModules[SIZE-1],tiles0.get(0).getModule());
    });
  }

  @Test
  void updateTilesModulesPerPageChanged() {
    robot.interact(() -> {
      // tiles get updated when modules per page get changed
      int halfSize = SIZE/2;
      setModulesPerPage(halfSize);

      assertEquals(halfSize, tiles0.size());
      assertEquals(halfSize, tiles1.size());

      // stays same, even if page indexes get changed
      page0.setPageIndex(1);
      page1.setPageIndex(0);

      assertEquals(halfSize, tiles0.size());
      assertEquals(halfSize, tiles1.size());
    });
  }

  @Test
  void updateTilesModulesListChanged() {
    robot.interact(() -> {
      // tiles get updated when list of modules gets changed


      assertEquals(modulesPerPage.get(), tiles0.size());
      assertEquals(SIZE - modulesPerPage.get(), tiles1.size());
    });
  }
}
