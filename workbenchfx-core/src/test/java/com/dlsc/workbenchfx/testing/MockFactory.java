package com.dlsc.workbenchfx.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import java.util.concurrent.CompletableFuture;
import javafx.scene.Node;

/**
 * Internal utility methods for producing mocks.
 */
public class MockFactory {

  /**
   * Internal method to create mocks for {@link WorkbenchModule}.
   *
   * @param displayNode node to be displayed in the mock
   * @param destroy     what the call for {@link WorkbenchModule#destroy()} should return
   * @param toString    what {@link WorkbenchModule#toString()} should return
   * @return the mock
   */
  public static WorkbenchModule createMockModule(Node displayNode, Node icon,
                                                 boolean destroy, String toString,
                                                 CompletableFuture<Boolean> moduleCloseable) {
    WorkbenchModule mockModule = mock(WorkbenchModule.class);
    when(mockModule.getName()).thenReturn(toString);
    when(mockModule.getIcon()).thenReturn(icon);
    when(mockModule.activate()).thenReturn(displayNode);
    when(mockModule.destroy()).thenReturn(destroy);
    when(mockModule.toString()).thenReturn(toString);
    when(moduleCloseable.getNow(false)).thenReturn(false);
    return mockModule;
  }

}
