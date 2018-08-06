package com.dlsc.workbenchfx.view.controls.module;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.testing.MockPage;
import com.dlsc.workbenchfx.testing.MockTile;
import java.util.concurrent.CompletableFuture;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Test for {@link Page}.
 */
class PageTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private static final int SIZE = 10;
  private WorkbenchModule[] mockModules = new WorkbenchModule[SIZE];
  private Node[] moduleNodes = new Node[SIZE];
  private int mockTileFactoryCalls = 0;
  private Tile[] mockTiles = new Tile[SIZE];
  private ObservableList<WorkbenchModule> modulesList;

  IntegerProperty modulesPerPage;

  private MockPage page0;
  private MockPage page1;
  private ObservableList<Tile> tiles0;
  private ObservableList<Tile> tiles1;

  @Mock
  private CompletableFuture<Boolean> mockModuleCloseable;

  @Override
  public void start(Stage stage) {
    MockitoAnnotations.initMocks(this);

    robot = new FxRobot();

    mockBench = mock(Workbench.class);

    for (int i = 0; i < moduleNodes.length; i++) {
      moduleNodes[i] = new Label("Module Content");
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = createMockModule(
          moduleNodes[i], null, true, "Module " + i, mockBench,
          FXCollections.observableArrayList(), FXCollections.observableArrayList()
      );
      MockTile mockTile = new MockTile(mockBench);
      mockTile.setModule(mockModules[i]);
      mockTiles[i] = mockTile;
    }
    when(mockBench.getTileFactory()).thenReturn(mockBench -> {
      Tile mockTile = mockTiles[mockTileFactoryCalls % SIZE];
      mockTileFactoryCalls++;
      return mockTile;
    });

    modulesList = FXCollections.observableArrayList(mockModules);
    when(mockBench.getModules()).thenReturn(modulesList);

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
      assertEquals(mockModules[0], tiles0.get(0).getModule());
      assertEquals(mockModules[SIZE - 1], tiles1.get(0).getModule());
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
      assertEquals(mockModules[0], tiles1.get(0).getModule());
      assertEquals(mockModules[SIZE - 1], tiles0.get(0).getModule());
    });
  }

  @Test
  void updateTilesModulesPerPageChanged() {
    robot.interact(() -> {
      // tiles get updated when modules per page get changed
      int halfSize = SIZE / 2;
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

      // remove first module
      modulesList.remove(0);

      assertEquals(modulesPerPage.get(), tiles0.size());
      assertEquals(0, tiles1.size());
      assertEquals(mockModules[1], tiles0.get(0).getModule());

      // add module again
      modulesList.add(0, mockModules[0]);
      assertEquals(modulesPerPage.get(), tiles0.size());
      assertEquals(mockModules[0], tiles0.get(0).getModule());

      // remove module range
      modulesList.remove(0, 5);

      assertEquals(5, tiles0.size());
      assertEquals(0, tiles1.size());
      assertEquals(mockModules[5], tiles0.get(0).getModule());
    });
  }
}
