package com.dlsc.workbenchfx.view.controls.module;

import static com.dlsc.workbenchfx.testing.MockFactory.createMockModule;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.testing.MockPage;
import com.dlsc.workbenchfx.testing.MockTab;
import com.dlsc.workbenchfx.testing.MockTile;
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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Created by François Martin on 05.06.18.
 */
class TabTest extends ApplicationTest {

  private FxRobot robot;
  private Workbench mockBench;
  private static final int SIZE = 10;
  private Module[] mockModules = new Module[SIZE];
  private Node[] moduleNodes = new Node[SIZE];
  private Node[] moduleIcons = new Node[SIZE];
  private ObservableList<Module> modulesList;

  private MockTab tab;

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    mockBench = mock(Workbench.class);

    for (int i = 0; i < moduleNodes.length; i++) {
      moduleNodes[i] = new Label("Module Content");
      moduleIcons[i] = new Label("Module Icon " + i);
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = createMockModule(moduleNodes[i], moduleIcons[i], true, "Module " + i);
    }

    modulesList = FXCollections.observableArrayList(mockModules);
    when(mockBench.getModules()).thenReturn(modulesList);

    tab = new MockTab(mockBench);
    tab.setModule(mockModules[0]);

    Scene scene = new Scene(tab, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void setModule() {
  }

  @Test
  void close() {
  }

  @Test
  void open() {
  }

  @Test
  void getModule() {
  }

  @Test
  void moduleProperty() {
  }

  @Test
  void getName() {
  }

  @Test
  void nameProperty() {
  }

  @Test
  void getIcon() {
  }

  @Test
  void iconProperty() {
  }

  @Test
  void isActiveTab() {
  }

  @Test
  void activeTabProperty() {
  }
}
