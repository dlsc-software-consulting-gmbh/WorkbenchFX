package com.dlsc.workbenchfx.model;

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
  void openModule() {

  }

  @Test
  void closeModule() {

  }

  @Test
  void closeModuleInvalid() {

  }
}
