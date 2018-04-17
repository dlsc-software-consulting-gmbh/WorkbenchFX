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
    }
    model = new WorkbenchFxModel(mockModules[FIRST_INDEX], mockModules[SECOND_INDEX], mockModules[LAST_INDEX]);

    first = mockModules[FIRST_INDEX];
    second = mockModules[SECOND_INDEX];
    last = mockModules[LAST_INDEX];
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
    verify(last, times(1)).init(model);
    verify(last, times(1)).activate();
    verify(last, never()).deactivate();
    // Open first (already initialized)
    model.openModule(first);
    assertSame(first,model.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX],model.getActiveModuleView());
    assertEquals(2, model.getOpenModules().size());
    verify(first, times(1)).init(model); // no additional init on first
    verify(last, times(1)).init(model); // no additional init on last
    inOrder = inOrder(first, last);
    inOrder.verify(last).deactivate();
    inOrder.verify(first).activate();
    verify(first, times(2)).activate();
    // Switch to home screen
    model.openHomeScreen();
    assertSame(null,model.getActiveModule());
    assertSame(null,model.getActiveModuleView());
    assertEquals(2, model.getOpenModules().size());
    verify(first, times(1)).init(model); // no additional init on first
    verify(last, times(1)).init(model); // no additional init on last
    verify(first, times(2)).deactivate();
    // Open second
    model.openModule(second);
    assertSame(second,model.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX],model.getActiveModuleView());
    assertEquals(3, model.getOpenModules().size());
    inOrder = inOrder(second);
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
  }

  @Test
  void openModuleInvalid() {
    /* Test if opening a module which has not been passed in the constructor of WorkbenchFxModel
    throws an exception */
    assertThrows(IllegalArgumentException.class, () -> model.openModule(mock(Module.class)));
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleOne() {
    // open and close module
    model.openModule(first);
    model.closeModule(first);
    
    assertSame(null,model.getActiveModule());
    assertSame(null,model.getActiveModuleView());
    assertEquals(0, model.getOpenModules().size());
    
    InOrder inOrder = inOrder(first);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.closeModule(first)
    inOrder.verify(first).deactivate();
    inOrder.verify(first).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleLeft1() {
    // open two modules, close left module
    // right active
    model.openModule(first);
    model.openModule(second);
    model.closeModule(first);
    
    assertSame(second,model.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX],model.getActiveModuleView());
    assertEquals(1, model.getOpenModules().size());
    verify(second, never()).deactivate();
    
    InOrder inOrder = inOrder(first, second);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
    // Call: model.closeModule(first)
    inOrder.verify(first).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleLeft2() {
    // open two modules, close left module
    // left active
    model.openModule(first);
    model.openModule(second);
    model.openModule(first);
    model.closeModule(first);
    
    assertSame(second,model.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX],model.getActiveModuleView());
    assertEquals(1, model.getOpenModules().size());
    
    InOrder inOrder = inOrder(first, second);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
    // Call: model.openModule(first)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // Call: model.closeModule(first)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).activate();
    inOrder.verify(first).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleRight1() {
    // open two modules, close right module
    // right active
    model.openModule(first);
    model.openModule(second);
    model.closeModule(second);
    
    assertSame(first,model.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX],model.getActiveModuleView());
    assertEquals(1, model.getOpenModules().size());
    
    InOrder inOrder = inOrder(first, second);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
    // Call: model.closeModule(second)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    inOrder.verify(second).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleRight2() {
    // open two modules, close right module
    // left active
    model.openModule(first);
    model.openModule(second);
    model.openModule(first);
    model.closeModule(second);
    
    assertSame(first,model.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX],model.getActiveModuleView());
    assertEquals(1, model.getOpenModules().size());
    
    InOrder inOrder = inOrder(first, second);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
    // Call: model.openModule(first)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // Call: model.closeModule(second)
    inOrder.verify(second).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModuleMiddleActive() {
    // open three modules and close middle module
    // middle active
    model.openModule(first);
    model.openModule(second);
    model.openModule(last);
    model.openModule(second);
    model.closeModule(second);

    assertSame(first,model.getActiveModule());
    assertSame(mockNodes[FIRST_INDEX],model.getActiveModuleView());
    assertEquals(2, model.getOpenModules().size());

    InOrder inOrder = inOrder(first, second, last);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
    // Call: model.openModule(last)
    inOrder.verify(second).deactivate();
    inOrder.verify(last).init(model);
    inOrder.verify(last).activate();
    // Call: model.openModule(second)
    inOrder.verify(last).deactivate();
    inOrder.verify(second).activate();
    // Call: model.closeModule(second)
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    inOrder.verify(second).destroy();
  }

  /**
   * Precondition: openModule tests pass.
   */
  @Test
  void closeModulePreventDestroy() {
    // open two modules, close right module
    // destroy() on second module will return false, so the module shouldn't get closed
    when(second.destroy()).thenReturn(false);
    model.openModule(first);
    model.openModule(second);
    model.closeModule(second);

    assertSame(second,model.getActiveModule());
    assertSame(mockNodes[SECOND_INDEX],model.getActiveModuleView());
    assertEquals(2, model.getOpenModules().size());

    InOrder inOrder = inOrder(first, second);
    // Call: model.openModule(first)
    inOrder.verify(first).init(model);
    inOrder.verify(first).activate();
    // Call: model.openModule(second)
    inOrder.verify(first).deactivate();
    inOrder.verify(second).init(model);
    inOrder.verify(second).activate();
    // Call: model.closeModule(second)
    // switch to first
    inOrder.verify(second).deactivate();
    inOrder.verify(first).activate();
    // destroy second
    inOrder.verify(second).destroy();
    // notice destroy() was unsuccessful, switching active module back to second
    inOrder.verify(first).deactivate();
    inOrder.verify(second).activate();
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
