package com.dlsc.workbenchfx.view.controls.module;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.testing.MockTab;
import java.util.concurrent.CompletableFuture;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Test for {@link Tab}.
 */
class TabTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private static final int SIZE = 10;
  private WorkbenchModule[] mockModules = new WorkbenchModule[SIZE];
  private Node[] moduleNodes = new Node[SIZE];
  private Node[] moduleIcons = new Node[SIZE];
  private ObservableList<WorkbenchModule> modulesList;
  private ObjectProperty<WorkbenchModule> activeModule;

  private MockTab tab;

  @Mock
  private CompletableFuture<Boolean> mockModuleCloseable;

  @Override
  public void start(Stage stage) {
    MockitoAnnotations.initMocks(this);

    robot = new FxRobot();

    mockBench = mock(Workbench.class);

    for (int i = 0; i < moduleNodes.length; i++) {
      moduleNodes[i] = new Label("Module Content");
      moduleIcons[i] = new Label("Module Icon " + i);
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = createMockModule(
          moduleNodes[i], moduleIcons[i], true, "Module " + i
      );
    }

    modulesList = FXCollections.observableArrayList(mockModules);
    when(mockBench.getModules()).thenReturn(modulesList);
    activeModule = new SimpleObjectProperty<>();
    when(mockBench.activeModuleProperty()).thenReturn(activeModule);

    tab = new MockTab(mockBench);
    tab.setModule(mockModules[0]);

    Scene scene = new Scene(tab, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void testActiveTabListener() {
    assertFalse(tab.isActiveTab());

    // change it to be the active module tab
    activeModule.set(mockModules[0]);
    assertTrue(tab.isActiveTab());

    // change the module displayed by this tab, should not be the active tab now
    tab.setModule(mockModules[1]);
    assertFalse(tab.isActiveTab());

    // changing the active module tab now should make it active again
    activeModule.set(mockModules[1]);
    assertTrue(tab.isActiveTab());

    verify(mockBench, atLeastOnce()).activeModuleProperty();
  }

  @Test
  void testModuleListener() {
    assertEquals("Module 0", tab.getName());
    assertEquals("Module Icon 0", ((Label) tab.getIcon()).getText());

    // change to module 1
    tab.setModule(mockModules[1]);
    assertEquals("Module 1", tab.getName());
    assertEquals("Module Icon 1", ((Label) tab.getIcon()).getText());
  }

  @Test
  void close() {
    // initial module
    tab.close();
    verify(mockBench).closeModule(mockModules[0]);

    // newly set module
    tab.setModule(mockModules[1]);
    tab.close();
    verify(mockBench).closeModule(mockModules[1]);
  }

  @Test
  void open() {
    // initial module
    tab.open();
    verify(mockBench).openModule(mockModules[0]);

    // newly set module
    tab.setModule(mockModules[1]);
    tab.open();
    verify(mockBench).openModule(mockModules[1]);
  }
}
