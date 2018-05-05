package com.dlsc.workbenchfx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * Created by François Martin on 20.03.18.
 */
@Tag("fast")
class WorkbenchFxTest {

  private static final int SIZE = 3;

  private static final int FIRST_INDEX = 0;
  private static final int SECOND_INDEX = 1;
  private static final int LAST_INDEX = SIZE - 1;
  WorkbenchFx workbench;

  Module[] mockModules = new Module[SIZE];
  Node[] moduleNodes = new Node[SIZE];

  Module first;
  Module second;
  Module last;
  private ObservableMap<Node, GlassPane> overlays;
  private ObservableSet<Node> blockingOverlaysShown;
  private ObservableSet<Node> overlaysShown;
  private Node overlay1;
  private Node overlay2;
  private Node overlay3;

  private MenuItem menuItem;
  private ObservableList<MenuItem> navigationDrawerItems;

  @BeforeEach
  void setUp() {
    // is needed to avoid "java.lang.IllegalStateException: Toolkit not initialized"
    JFXPanel jfxPanel = new JFXPanel();

    for (int i = 0; i < moduleNodes.length; i++) {
      moduleNodes[i] = new Label("Module Content");
    }

    for (int i = 0; i < mockModules.length; i++) {
      mockModules[i] = mock(Module.class);
      when(mockModules[i].activate()).thenReturn(moduleNodes[i]);
      when(mockModules[i].destroy()).thenReturn(true);
      when(mockModules[i].toString()).thenReturn("Module " + i);
    }

    workbench = WorkbenchFx.builder(mockModules[FIRST_INDEX], mockModules[SECOND_INDEX],
        mockModules[LAST_INDEX])
        // use "module.getName()" twice, to differentiate between tab and tile factories
        .tabFactory(
            (workbench, module) -> new Label(module.getName() + module.getName(), module.getIcon()))
        .tileFactory((workbench, module) -> new Label(module.getName(), module.getIcon()))
        .pageFactory((workbench, pageIndex) -> new Label(pageIndex.toString()))
        .navigationDrawer(menuItem)
        .build();

    first = mockModules[FIRST_INDEX];
    second = mockModules[SECOND_INDEX];
    last = mockModules[LAST_INDEX];

    overlays = workbench.getOverlays();
    blockingOverlaysShown = workbench.getBlockingOverlaysShown();
    overlaysShown = workbench.getOverlaysShown();
    overlay1 = new Label();
    overlay1.setVisible(false);
    overlay2 = new Label();
    overlay2.setVisible(false);
    overlay3 = new Label();
    overlay3.setVisible(false);

    navigationDrawerItems = workbench.getNavigationDrawerItems();
  }

  @Test
  void testCtor() {
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
    assertSame(moduleNodes[FIRST_INDEX], workbench.getActiveModuleView());
    assertEquals(1, workbench.getOpenModules().size());
    InOrder inOrder = inOrder(first);
    inOrder.verify(first).init(workbench);
    inOrder.verify(first).activate();
    // Open last
    workbench.openModule(last);
    assertSame(last, workbench.getActiveModule());
    assertSame(moduleNodes[LAST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());
    inOrder = inOrder(first, last);
    inOrder.verify(first).deactivate();
    inOrder.verify(last).init(workbench);
    inOrder.verify(last).activate();
    // Open last again
    workbench.openModule(last);
    assertSame(last, workbench.getActiveModule());
    assertSame(moduleNodes[LAST_INDEX], workbench.getActiveModuleView());
    assertEquals(2, workbench.getOpenModules().size());
    verify(last, times(1)).init(workbench);
    verify(last, times(1)).activate();
    verify(last, never()).deactivate();
    // Open first (already initialized)
    workbench.openModule(first);
    assertSame(first, workbench.getActiveModule());
    assertSame(moduleNodes[FIRST_INDEX], workbench.getActiveModuleView());
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
    assertSame(moduleNodes[SECOND_INDEX], workbench.getActiveModuleView());
    assertEquals(3, workbench.getOpenModules().size());
    inOrder = inOrder(second);
    inOrder.verify(second).init(workbench);
    inOrder.verify(second).activate();
    inOrder.verifyNoMoreInteractions();
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[SECOND_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[SECOND_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[FIRST_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[FIRST_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[FIRST_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[SECOND_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[SECOND_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[SECOND_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
    assertSame(moduleNodes[FIRST_INDEX], workbench.getActiveModuleView());
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
    inOrder.verifyNoMoreInteractions();
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
  void closeInactiveModule() {
    workbench.openModule(first);
    workbench.openModule(second);
    workbench.openModule(last);
    workbench.closeModule(second);

    assertSame(last, workbench.getActiveModule());
    assertSame(moduleNodes[LAST_INDEX], workbench.getActiveModuleView());
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
    // Call: workbench.closeModule(second)
    inOrder.verify(second).destroy();
    inOrder.verifyNoMoreInteractions();
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

  @Test
  void amountOfPages() {
    int modulesPerPage = 1;
    assertEquals(1, prepareWorkbench(1, modulesPerPage).amountOfPages());
    assertEquals(2, prepareWorkbench(2, modulesPerPage).amountOfPages());
    assertEquals(3, prepareWorkbench(3, modulesPerPage).amountOfPages());

    modulesPerPage = 2;
    assertEquals(1, prepareWorkbench(1, modulesPerPage).amountOfPages());
    assertEquals(1, prepareWorkbench(2, modulesPerPage).amountOfPages());
    assertEquals(2, prepareWorkbench(3, modulesPerPage).amountOfPages());
    assertEquals(2, prepareWorkbench(4, modulesPerPage).amountOfPages());
    assertEquals(3, prepareWorkbench(5, modulesPerPage).amountOfPages());

    modulesPerPage = 3;
    assertEquals(1, prepareWorkbench(1, modulesPerPage).amountOfPages());
    assertEquals(1, prepareWorkbench(2, modulesPerPage).amountOfPages());
    assertEquals(1, prepareWorkbench(3, modulesPerPage).amountOfPages());
    assertEquals(2, prepareWorkbench(4, modulesPerPage).amountOfPages());
    assertEquals(2, prepareWorkbench(5, modulesPerPage).amountOfPages());
    assertEquals(2, prepareWorkbench(6, modulesPerPage).amountOfPages());
    assertEquals(3, prepareWorkbench(7, modulesPerPage).amountOfPages());
  }

  private WorkbenchFx prepareWorkbench(int moduleAmount, int modulesPerPage) {
    Module[] modules = new Module[moduleAmount];
    for (int i = 0; i < moduleAmount; i++) {
      modules[i] = mock(Module.class);
    }
    return WorkbenchFx.builder(modules).modulesPerPage(modulesPerPage).build();
  }

  @Test
  void builder() {
    WorkbenchFxBuilder builder = WorkbenchFx.builder();
    assertNotNull(builder);
  }

  @Test
  void getTab() {
    verify(first, never()).getName();
    verify(first, never()).getIcon();
    Node tab = workbench.getTab(first);
    assertNotNull(tab);
    verify(first, times(2)).getName();
    verify(first).getIcon();
  }

  @Test
  void getTile() {
    verify(first, never()).getName();
    verify(first, never()).getIcon();
    Node tab = workbench.getTile(first);
    assertNotNull(tab);
    verify(first).getName();
    verify(first).getIcon();
  }

  @Test
  void getPage() {
    Node page = workbench.getPage(0);
    assertTrue(page instanceof Label);
    assertEquals("0", ((Label) page).getText());
    assertEquals("5", ((Label) workbench.getPage(5)).getText());
  }

  @Test
  void getOverlays() {
    ObservableMap<Node, GlassPane> overlays = workbench.getOverlays();
    // Test if unmodifiable map is returned
    assertThrows(UnsupportedOperationException.class,
        () -> overlays.put(new Label(), new GlassPane()));
  }

  @Test
  void showOverlayBlocking() {
    assertEquals(0, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());

    boolean result = workbench.showOverlay(overlay1, true);

    assertTrue(result);
    assertEquals(1, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertTrue(overlay1.isVisible()); // overlay1 has been made visible
    GlassPane glassPane = overlays.get(overlay1);
    assertFalse(glassPane.isHide());
    assertNull(glassPane.onMouseClickedProperty().get()); // no closing handler has been attached
    assertTrue(glassPane.hideProperty().isBound());

    // test visibility binding to GlassPane
    overlay1.setVisible(false);
    assertTrue(glassPane.isHide());

    // test if calling showOverlay again, even though it's already showing, does anything
    result = workbench.showOverlay(overlay1, true);

    assertFalse(result);
    assertEquals(1, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is still invisible
  }

  @Test
  void showOverlayNonBlocking() {
    assertEquals(0, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());

    boolean result = workbench.showOverlay(overlay1, false);

    assertTrue(result);
    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(1, overlaysShown.size());
    assertTrue(overlay1.isVisible()); // overlay1 has been made visible
    GlassPane glassPane = overlays.get(overlay1);
    assertFalse(glassPane.isHide());
    assertNotNull(glassPane.onMouseClickedProperty().get()); // closing handler has been attached
    assertTrue(glassPane.hideProperty().isBound());

    // test visibility binding to GlassPane
    overlay1.setVisible(false);
    assertTrue(glassPane.isHide());

    // test if calling showOverlay again, even though it's already showing, does anything
    result = workbench.showOverlay(overlay1, false);

    assertFalse(result);
    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(1, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is still invisible
  }

  @Test
  void showOverlayMultiple() {
    assertEquals(0, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());

    boolean result = workbench.showOverlay(overlay1, false);
    assertTrue(result);

    result = workbench.showOverlay(overlay2, true);
    assertTrue(result);

    assertEquals(2, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(1, overlaysShown.size());
    assertTrue(overlay1.isVisible()); // overlay1 has been made visible
    assertTrue(overlay2.isVisible()); // overlay1 has been made visible
    GlassPane glassPane1 = overlays.get(overlay1);
    assertFalse(glassPane1.isHide());
    assertNotNull(glassPane1.onMouseClickedProperty().get()); // closing handler has been attached
    assertTrue(glassPane1.hideProperty().isBound());
    GlassPane glassPane2 = overlays.get(overlay2);
    assertFalse(glassPane2.isHide());
    assertNull(glassPane2.onMouseClickedProperty().get()); // no closing handler has been attached
    assertTrue(glassPane2.hideProperty().isBound());

    // test visibility binding to GlassPane
    overlay1.setVisible(false);
    assertTrue(glassPane1.isHide());
    overlay2.setVisible(false);
    assertTrue(glassPane2.isHide());

    // test if calling showOverlay again, even though it's already showing, does anything
    result = workbench.showOverlay(overlay1, false);
    assertFalse(result);
    result = workbench.showOverlay(overlay2, true);
    assertFalse(result);

    assertEquals(2, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(1, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is still invisible
    assertFalse(overlay2.isVisible()); // overlay1 is still invisible
  }

  /**
   * Precondition: showOverlay tests pass.
   */
  @Test
  void hideOverlayBlocking() {
    workbench.showOverlay(overlay1, true);
    boolean result = workbench.hideOverlay(overlay1, true);

    assertTrue(result);
    assertEquals(1, overlays.size()); // still loaded
    assertEquals(0, blockingOverlaysShown.size()); // none shown
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is invisible
    GlassPane glassPane = overlays.get(overlay1);
    assertTrue(glassPane.isHide());
    assertTrue(glassPane.hideProperty().isBound());

    // test if calling hideOverlay again, even though it's already hidden, does anything
    result = workbench.hideOverlay(overlay1, true);

    assertFalse(result);
    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is still invisible

    // test if calling showOverlay again, doesn't load the overlay1 into the map again
    result = workbench.showOverlay(overlay1, true);

    assertTrue(result);
    assertEquals(1, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertTrue(overlay1.isVisible()); // overlay1 is visible again
  }

  /**
   * Precondition: showOverlay tests pass.
   */
  @Test
  void hideOverlayNonBlocking() {
    workbench.showOverlay(overlay1, false);
    boolean result = workbench.hideOverlay(overlay1, false);

    assertTrue(result);
    assertEquals(1, overlays.size()); // still loaded
    assertEquals(0, blockingOverlaysShown.size()); // none shown
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is invisible
    GlassPane glassPane = overlays.get(overlay1);
    assertTrue(glassPane.isHide());
    assertTrue(glassPane.hideProperty().isBound());

    // test if calling hideOverlay again, even though it's already hidden, does anything
    result = workbench.hideOverlay(overlay1, false);

    assertFalse(result);
    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible()); // overlay1 is still invisible

    // test if calling showOverlay again, doesn't load the overlay1 into the map again
    result = workbench.showOverlay(overlay1, false);

    assertTrue(result);
    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(1, overlaysShown.size());
    assertTrue(overlay1.isVisible()); // overlay1 is visible again
  }

  /**
   * Precondition: showOverlay tests pass.
   */
  @Test
  void clearOverlaysShowing() {
    workbench.showOverlay(overlay1, false);
    workbench.showOverlay(overlay2, true);
    workbench.showOverlay(overlay3, false);

    assertEquals(3, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(2, overlaysShown.size());
    assertTrue(overlay1.isVisible());
    assertTrue(overlay2.isVisible());
    assertTrue(overlay3.isVisible());

    final GlassPane glassPane1 = overlays.get(overlay1);
    final GlassPane glassPane2 = overlays.get(overlay2);
    final GlassPane glassPane3 = overlays.get(overlay3);

    workbench.clearOverlays();

    assertEquals(0, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible());
    assertFalse(overlay2.isVisible());
    assertFalse(overlay3.isVisible());
    // closing handler was removed for non-blocking
    assertNull(glassPane1.onMouseClickedProperty().get());
    assertNull(glassPane3.onMouseClickedProperty().get());
    // glass panes were unbound
    assertFalse(glassPane1.hideProperty().isBound());
    assertFalse(glassPane2.hideProperty().isBound());
    assertFalse(glassPane3.hideProperty().isBound());
  }

  /**
   * Precondition: showOverlay tests pass.
   */
  @Test
  void clearOverlaysHiding() {
    workbench.showOverlay(overlay1, false);
    workbench.showOverlay(overlay2, true);
    workbench.showOverlay(overlay3, false);

    assertEquals(3, overlays.size());
    assertEquals(1, blockingOverlaysShown.size());
    assertEquals(2, overlaysShown.size());
    assertTrue(overlay1.isVisible());
    assertTrue(overlay2.isVisible());
    assertTrue(overlay3.isVisible());

    workbench.hideOverlay(overlay1, false);
    workbench.hideOverlay(overlay2, true);
    workbench.hideOverlay(overlay3, false);

    assertEquals(3, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible());
    assertFalse(overlay2.isVisible());
    assertFalse(overlay3.isVisible());

    final GlassPane glassPane1 = overlays.get(overlay1);
    final GlassPane glassPane2 = overlays.get(overlay2);
    final GlassPane glassPane3 = overlays.get(overlay3);

    workbench.clearOverlays();

    assertEquals(0, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(overlay1.isVisible());
    assertFalse(overlay2.isVisible());
    assertFalse(overlay3.isVisible());
    // closing handler was removed for non-blocking
    assertNull(glassPane1.onMouseClickedProperty().get());
    assertNull(glassPane3.onMouseClickedProperty().get());
    // glass panes were unbound
    assertFalse(glassPane1.hideProperty().isBound());
    assertFalse(glassPane2.hideProperty().isBound());
    assertFalse(glassPane3.hideProperty().isBound());
  }

  @Test
  void showNavigationDrawer() {
    Node navigationDrawer = workbench.getNavigationDrawer();
    navigationDrawer.setVisible(false);

    workbench.showNavigationDrawer();

    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(1, overlaysShown.size());
    assertTrue(navigationDrawer.isVisible());
  }

  @Test
  void hideNavigationDrawer() {
    Node navigationDrawer = workbench.getNavigationDrawer();
    navigationDrawer.setVisible(false);

    workbench.showNavigationDrawer();
    workbench.hideNavigationDrawer();

    assertEquals(1, overlays.size());
    assertEquals(0, blockingOverlaysShown.size());
    assertEquals(0, overlaysShown.size());
    assertFalse(navigationDrawer.isVisible());
  }

  @Test
  void getNavigationDrawerItems() {
    assertEquals(1, navigationDrawerItems.size());
    assertEquals(menuItem, navigationDrawerItems.get(0));
  }

  /**
   * Precondition: getNavigationDrawerItems tests pass.
   */
  @Test
  void addNavigationDrawerItems() {
    workbench.addNavigationDrawerItems(menuItem);
    assertEquals(2, navigationDrawerItems.size());
    assertEquals(menuItem, navigationDrawerItems.get(1));
  }

  /**
   * Precondition: getNavigationDrawerItems tests pass.
   */
  @Test
  void removeNavigationDrawerItems() {
    workbench.removeNavigationDrawerItems(menuItem);
    assertEquals(0, navigationDrawerItems.size());
  }
}
