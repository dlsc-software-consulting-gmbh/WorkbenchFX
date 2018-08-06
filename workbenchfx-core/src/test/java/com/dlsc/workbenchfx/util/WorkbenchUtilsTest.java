package com.dlsc.workbenchfx.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link WorkbenchUtils}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchUtilsTest {

  ObservableList<String> observableList;

  @BeforeEach
  void setUp() {
    // is needed to avoid "java.lang.IllegalStateException: Toolkit not initialized"
    JFXPanel jfxPanel = new JFXPanel();
    observableList = FXCollections.observableArrayList();
  }

  @Test
  void assertNodeNotSame() {
    Node node1 = new Button();
    Node node2 = new Button();
    WorkbenchUtils.assertNodeNotSame(node1, node2); // should not throw
    assertThrows(
        IllegalArgumentException.class,
        () -> WorkbenchUtils.assertNodeNotSame(node1, node1)
    );
  }

  @Test
  void addSetListenerAdd() {
    Thread mock = mock(Thread.class);
    WorkbenchUtils.addListListener(
        observableList,
        c -> mock.run(),
        c -> fail("was removed instead of added")
    );
    observableList.add("Test");
    verify(mock).run();
  }

  @Test
  void addSetListenerRemove() {
    Thread mock = mock(Thread.class);
    String test = "Test";
    observableList.add(test);
    WorkbenchUtils.addListListener(
        observableList,
        c -> fail("was added instead of removed"),
        c -> mock.run()
    );
    observableList.remove(test);
    verify(mock).run();
  }

  @Test
  void convertToId() {
    String[] toBeConverted = {
        "", "abc", "françois", "aeiouäöü", "üüü", "aB0 -", "My Pokémon Module", "+''--12a?`bcTTT",
        "\\hello", "+*ç%&/()=", "hello\nworld", "ﯠﯡﯢﯦﯞﯫﯭﻠﻦ", "\u0044DDD", "Hello\tWorld",
        "Rhøthgar's Modul", "\u0126\u0117\u013C\u013C\u00F8\u005F\u0057\u006F\u0072\u006C\u0064"
    };
    String[] expectedIds   = {
        "", "abc", "franois",  "aeiou",    "",    "ab0--", "my-pokmon-module",  "--12abcttt",
        "hello",   "",          "helloworld",   "",          "dddd",      "helloworld",
        "rhthgars-modul",   "world"
    };

    for (int i = 0; i < toBeConverted.length; i++) {
      assertEquals(expectedIds[i], WorkbenchUtils.convertToId(toBeConverted[i]));
    }
  }

  @Test
  void calculateColumnsPerRow() {
    int[] modulesPerPage = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    int[] columnsPerRow =  {0, 1, 2, 3, 2, 3, 3, 3, 3, 3,  4,  4,  4};
    for (int i = 0; i < modulesPerPage.length; i++) {
      assertEquals(columnsPerRow[i], WorkbenchUtils.calculateColumnsPerRow(modulesPerPage[i]));
    }
  }
}
