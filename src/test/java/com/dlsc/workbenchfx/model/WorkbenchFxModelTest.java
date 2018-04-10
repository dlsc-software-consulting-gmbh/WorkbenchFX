package com.dlsc.workbenchfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.model.module.Module;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * Created by François Martin on 20.03.18.
 */
@Tag("fast")
class WorkbenchFxModelTest {

  private final static int SIZE = 3;

  private static final int FIRST_INDEX = 0;
  private static final int SECOND_INDEX = 1;
  private static final int LAST_INDEX = SIZE-1;
  WorkbenchFxModel model;

  Module[] mockModules = new Module[SIZE];
  Node[] mockNodes = new Node[SIZE];

  @BeforeEach
  void setUp() {
    // is needed to avoid "java.lang.IllegalStateException: Toolkit not initialized"
    JFXPanel jfxPanel = new JFXPanel();

    for (int i = 0; i < mockNodes.length; i++) {
      mockNodes[i] = mock(Node.class);
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = mock(Module.class);
      when(mockModules[i].activate()).thenReturn(mockNodes[i]);
      when(mockModules[i].destroy()).thenReturn(true);
    }
    model = new WorkbenchFxModel(mockModules[FIRST_INDEX], mockModules[SECOND_INDEX], mockModules[LAST_INDEX]);
  }

  @Test
  void testCtor(){
    assertEquals(mockModules.length, model.getModules().size());
    for (int i = 0; i < mockModules.length; i++) {
      assertSame(mockModules[i], model.getModules().get(i));
    }

    assertEquals(0, model.getOpenModules().size());

    assertNull(model.activeModuleViewProperty().get());
  }

  @Test
  void openModule() {
    Module first = mockModules[FIRST_INDEX];
    Module second = mockModules[SECOND_INDEX];
    Module last = mockModules[LAST_INDEX];
    // Open first
    model.openModule(first);
    assertSame(first,model.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX],model.getActiveModuleView());
    assertEquals(1, model.getOpenModules().size());
    InOrder inOrder = inOrder(first);
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Open last
    model.openModule(last);
    assertSame(last,model.getActiveModule());
    assertSame(mockNodes[LAST_INDEX],model.getActiveModuleView());
    assertEquals(2, model.getOpenModules().size());
    inOrder = inOrder(first, last);
    inOrder.verify(first).deactivate();
    inOrder.verify(last).init(model);
    inOrder.verify(last).activate();
    // Open last again
    model.openModule(last);
    assertSame(last,model.getActiveModule());
    assertSame(mockNodes[LAST_INDEX],model.getActiveModuleView());
    assertEquals(2, model.getOpenModules().size());
    verify(last, calls(0)).init(model);
    verify(last, calls(0)).deactivate();
    // Open first (already initialized)

    // Switch to home screen

    // Open second

  }

  @Test
  void openModuleInvalid() {
    /* Test if opening a module which has not been passed in the constructor of WorkbenchFxModel
    throws an exception */
    assertThrows(IllegalArgumentException.class, () -> model.openModule(mock(Module.class)));
  }

  @Test
  void closeModule() {

  }

  @Test
  void closeModuleInvalid() {
    // Test for null
    assertThrows(NullPointerException.class, () -> model.closeModule(null));
    // Test if closing a module not included in the modules at all throws an exception
    assertThrows(IllegalArgumentException.class, () -> model.closeModule(mock(Module.class)));
    // Test if closing a module not opened throws an exception
    assertThrows(IllegalArgumentException.class, () -> model.closeModule(mockModules[0]));
  }

  @Test
  void getOpenModules() {
    ObservableList<Module> modules = model.getOpenModules();
    // Test if unmodifiable list is returned
    assertThrows(UnsupportedOperationException.class, () -> modules.remove(0));
  }

  @Test
  void getModules() {
    ObservableList<Module> modules = model.getModules();
    // Test if unmodifiable list is returned
    assertThrows(UnsupportedOperationException.class, () -> modules.remove(0));
  }

  @Test
  void activeModuleViewProperty() {
    assertTrue(model.activeModuleViewProperty() instanceof ReadOnlyObjectProperty);
  }

  @Test
  void activeModuleProperty() {
    assertTrue(model.activeModuleProperty() instanceof ReadOnlyObjectProperty);
  }
}
