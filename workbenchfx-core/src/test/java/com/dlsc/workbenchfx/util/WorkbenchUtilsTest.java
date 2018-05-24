package com.dlsc.workbenchfx.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
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
public class WorkbenchUtilsTest {

  ObservableSet<String> observableSet;

  @BeforeEach
  void setUp() {
    // is needed to avoid "java.lang.IllegalStateException: Toolkit not initialized"
    JFXPanel jfxPanel = new JFXPanel();
    observableSet = FXCollections.observableSet();
  }

  @Test
  void assertNodeNotSame() {
    Node node1 = new Button();
    Node node2 = new Button();
    WorkbenchFxUtils.assertNodeNotSame(node1, node2); // should not throw
    assertThrows(
        IllegalArgumentException.class,
        () -> WorkbenchFxUtils.assertNodeNotSame(node1, node1)
    );
  }

  @Test
  void addSetListenerAdd() {
    Thread mock = mock(Thread.class);
    WorkbenchFxUtils.addSetListener(
        observableSet,
        c -> mock.run(),
        c -> fail("was removed instead of added")
    );
    observableSet.add("Test");
    verify(mock).run();
  }

  @Test
  void addSetListenerRemove() {
    Thread mock = mock(Thread.class);
    String test = "Test";
    observableSet.add(test);
    WorkbenchFxUtils.addSetListener(
        observableSet,
        c -> fail("was added instead of removed"),
        c -> mock.run()
    );
    observableSet.remove(test);
    verify(mock).run();
  }
}
