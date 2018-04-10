package com.dlsc.workbenchfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.model.module.Module;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Created by François Martin on 20.03.18.
 */
@Tag("fast")
class WorkbenchFxModelTest {

  WorkbenchFxModel model;

  Module[] mockModules = new Module[3];
  Node[] mockNodes = new Node[3];

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
    model = new WorkbenchFxModel(mockModules[0], mockModules[1], mockModules[2]);
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
}
