package com.dlsc.workbenchfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.module.Module;
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
class WorkbenchFxTest {

  private final static int SIZE = 3;

  private static final int FIRST_INDEX = 0;
  private static final int SECOND_INDEX = 1;
  private static final int LAST_INDEX = SIZE-1;
  WorkbenchFx workbench;

  Module[] mockModules = new Module[SIZE];
  Node[] mockNodes = new Node[SIZE];

  Module first;
  Module second;
  Module last;

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
      when(mockModules[i].getTile()).thenReturn(mock(Node.class));
      when(mockModules[i].getTab()).thenReturn(mock(Node.class));
    }
    workbench = WorkbenchFx.of(mockModules[FIRST_INDEX], mockModules[SECOND_INDEX], mockModules[LAST_INDEX]);

    first = mockModules[FIRST_INDEX];
    second = mockModules[SECOND_INDEX];
    last = mockModules[LAST_INDEX];
  }

  @Test
  void testCtor(){
    assertEquals(mockModules.length, workbench.getModules().size());
    for (int i = 0; i < mockModules.length; i++) {
      assertSame(mockModules[i], workbench.getModules().get(i));
    }

    assertEquals(0, workbench.getOpenModules().size());

    assertNull(workbench.activeModuleViewProperty().get());
  }

  @Test
  void openModule() {
    // Open first
    workbench.openModule(first);
    assertSame(first, workbench.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());
    InOrder inOrder = inOrder(first);
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Open last
    workbench.openModule(last);
    assertSame(last, workbench.getActiveModule());
    assertSame(mockNodes[LAST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());
    inOrder = inOrder(first, last);
    inOrder.verify(first).deactivate();
    inOrder.verify(last).init(workbench);
    inOrder.verify(last).activate();
    // Open last again
    workbench.openModule(last);
    assertSame(last, workbench.getActiveModule());
    assertSame(mockNodes[LAST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());
    verify(last, times(1)).init(workbench);
    verify(last, times(1)).activate();
    verify(last, never()).deactivate();
    // Open first (already initialized)
    workbench.openModule(first);
    assertSame(first, workbench.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());
    verify(first, times(1)).init(workbench); // no additional init on first
    verify(last, times(1)).init(workbench); // no additional init on last
    inOrder = inOrder(first, last);
    inOrder.verify(last).deactivate();
    inOrder.verify(first).activate();
    verify(first, times(2)).activate();
    // Switch to home screen
    workbench.openHomeScreen();
    assertSame(null, workbench.getActiveModule());
    assertSame(null, workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());
    verify(first, times(1)).init(workbench); // no additional init on first
    verify(last, times(1)).init(workbench); // no additional init on last
    verify(first, times(2)).deactivate();
    // Open second
    workbench.openModule(second);
    assertSame(second, workbench.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(3, workbench.getOpenModules().size());
    inOrder = inOrder(second);
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
  }

  @Test
  void openModuleInvalid() {
    /* Test if opening a module which has not been passed in the constructor of WorkbenchFxModel
    throws an exception */
    assertThrows(IllegalArgumentException.class, () -> workbench.openModule(mock(Module.class)));
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleOne() {
    // open and close module
    workbench.openModule(first);
    workbench.closeModule(first);

    assertSame(null, workbench.getActiveModule());
    assertSame(null, workbench.getActiveModuleView());
    assertEquals(0, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.closeModule(first)
    inOrder.verify(first).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleLeft1() {
    // open two modules, close left module
    // right active
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.closeModule(first);

    assertSame(second, workbench.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());
    verify(second, never()).deactivate();

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(first)
    inOrder.verify(first).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleLeft2() {
    // open two modules, close left module
    // left active
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.openModule(first);
    workbench.closeModule(first);

    assertSame(second, workbench.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.openModule(first)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // Call: workbench.closeModule(first)
    inOrder.verify(first).destroy();
    inOrder.verify(second).activate();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleRight1() {
    // open two modules, close right module
    // right active
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.closeModule(second);

    assertSame(first, workbench.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(second)
    inOrder.verify(second).destroy();
    inOrder.verify(first).activate();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleRight2() {
    // open two modules, close right module
    // left active
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.openModule(first);
    workbench.closeModule(second);

    assertSame(first, workbench.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.openModule(first)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // Call: workbench.closeModule(second)
    inOrder.verify(second).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleMiddleActive() {
    // open three modules and close middle module
    // middle active
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.openModule(last);
    workbench.openModule(second);
    workbench.closeModule(second);

    assertSame(first, workbench.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second, last);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.openModule(last)
    inOrder.verify(second).deactivate();
    inOrder.verify(last).init(workbench);
    inOrder.verify(last).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(last).deactivate();
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(second)
    inOrder.verify(second).destroy();
    inOrder.verify(first).activate();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModulePreventDestroyActive() {
    // open two modules, close second (active) module
    // destroy() on second module will return false, so the module shouldn't get closed
    when(second.destroy()).thenReturn(false);
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.closeModule(second);

    assertSame(second, workbench.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(second)
    // destroy second
    inOrder.verify(second).destroy();
    // notice destroy() was unsuccessful, keep focus on second
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModulePreventDestroyInactive() {
    // open two modules, close first (inactive) module
    // destroy() on first module will return false, so the module shouldn't get closed
    when(first.destroy()).thenReturn(false);
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.closeModule(first);

    assertSame(second, workbench.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(second)
    // destroy second
    inOrder.verify(first).destroy();
    // notice destroy() was unsuccessful, keep focus on second
  }

  /**
   * Example of what happens in case of a closing dialog in the destroy() method of a module
   * with the user confirming the module should get closed.
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleDestroyInactiveDialogClose() {
    // open two modules, close first (inactive) module
    // destroy() on first module will return false, so the module shouldn't get closed
    when(first.destroy()).then(invocation -> {
          workbench.openModule(first);
          // dialog opens, user confirms closing module
          return true;
        });
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.closeModule(first);

    assertSame(second, workbench.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(first)
    // attempt to destroy first
    inOrder.verify(first).destroy();
    // destroy() opens itself: workbench.openModule(first)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // destroy() returns true, switch to second
    inOrder.verify(second).activate();
  }

  /**
   * Example of what happens in case of a closing dialog in the destroy() method of a module
   * with the user confirming the module should NOT get closed.
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModulePreventDestroyInactiveDialogClose() {
    // open two modules, close first (inactive) module
    // destroy() on first module will return false, so the module shouldn't get closed
    when(first.destroy()).then(invocation -> {
      workbench.openModule(first);
      // dialog opens, user confirms NOT closing module
      return false;
    });
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.closeModule(first);

    assertSame(first, workbench.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: workbench.openModule(first)
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Call: workbench.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    // Call: workbench.closeModule(first)
    // attempt to destroy first
    inOrder.verify(first).destroy();
    // destroy() opens itself: workbench.openModule(first)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // destroy() returns false, first stays the active module
  }

  @Test
  void closeModuleInvalid() {
    // Test for null
    assertThrows(NullPointerException.class, () -> workbench.closeModule(null));
    // Test if closing a module not included in the modules at all throws an exception
    assertThrows(IllegalArgumentException.class, () -> workbench.closeModule(mock(Module.class)));
    // Test if closing a module not opened throws an exception
    assertThrows(IllegalArgumentException.class, () -> workbench.closeModule(mockModules[0]));
  }

  @Test
  void getOpenModules() {
    ObservableList<Module> modules = workbench.getOpenModules();
    // Test if unmodifiable list is returned
    assertThrows(UnsupportedOperationException.class, () -> modules.remove(0));
  }

  @Test
  void getModules() {
    ObservableList<Module> modules = workbench.getModules();
    // Test if unmodifiable list is returned
    assertThrows(UnsupportedOperationException.class, () -> modules.remove(0));
  }

  @Test
  void activeModuleViewProperty() {
    assertTrue(workbench.activeModuleViewProperty() instanceof ReadOnlyObjectProperty);
  }

  @Test
  void activeModuleProperty() {
    assertTrue(workbench.activeModuleProperty() instanceof ReadOnlyObjectProperty);
  }
}
