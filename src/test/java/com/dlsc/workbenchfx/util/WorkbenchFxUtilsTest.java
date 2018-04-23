package com.dlsc.workbenchfx.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link WorkbenchFxUtils}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxUtilsTest {

  @BeforeEach
  void setUp() {
    // is needed to avoid "java.lang.IllegalStateException: Toolkit not initialized"
    JFXPanel jfxPanel = new JFXPanel();
  }

  @Test
  void assertNodeNotSame() {
    Node node1 = new Button();
    Node node2 = new Button();
    WorkbenchFxUtils.assertNodeNotSame(node1, node2); // should not throw
    assertThrows(IllegalArgumentException.class,
        () -> WorkbenchFxUtils.assertNodeNotSame(node1, node1)
    );
  }
}
