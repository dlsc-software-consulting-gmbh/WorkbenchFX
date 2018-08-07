package com.dlsc.workbenchfx.view.controls;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.testing.MockNavigationDrawer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Test for {@link NavigationDrawer}.
 */
class NavigationDrawerTest extends ApplicationTest {

  private static final double WIDTH = 100;

  private FxRobot robot;
  private Workbench mockBench;

  private MockNavigationDrawer navigationDrawer;

  private ObservableList<MenuItem> items = FXCollections.observableArrayList();
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(WIDTH);

  @Override
  public void start(Stage stage) {
    robot = new FxRobot();

    mockBench = mock(Workbench.class);
    when(mockBench.getNavigationDrawerItems()).thenReturn(items);
    when(mockBench.getWidth()).thenReturn(WIDTH);
    when(mockBench.widthProperty()).thenReturn(widthProperty);

    navigationDrawer = new MockNavigationDrawer();
    navigationDrawer.setWorkbench(mockBench);

    Scene scene = new Scene(navigationDrawer, 100, 100);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void hide() {
    navigationDrawer.hide();
    verify(mockBench).hideNavigationDrawer();
  }

  @Test
  void getItems() {
    ObservableList<MenuItem> items = navigationDrawer.getItems();
    verify(mockBench).getNavigationDrawerItems();
    assertSame(this.items, items);
  }

  @Test
  void workbenchWidthProperty() {
    ReadOnlyDoubleProperty widthProperty = navigationDrawer.workbenchWidthProperty();
    verify(mockBench).widthProperty();
    assertSame(this.widthProperty, widthProperty);
  }
}
