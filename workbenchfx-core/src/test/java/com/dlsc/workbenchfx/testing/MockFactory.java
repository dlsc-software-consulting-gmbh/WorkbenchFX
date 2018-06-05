package com.dlsc.workbenchfx.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.module.Module;
import javafx.scene.Node;

/**
 * Internal utility methods for producing mocks.
 */
public class MockFactory {

  /**
   * Internal method to create mocks for {@link Module}.
   *
   * @param displayNode node to be displayed in the mock
   * @param destroy     what the call for {@link Module#destroy()} should return
   * @param toString    what {@link Module#toString()} should return
   * @return the mock
   */
  public static Module createMockModule(Node displayNode, Node icon,
                                        boolean destroy, String toString) {
    Module mockModule = mock(Module.class);
    when(mockModule.getName()).thenReturn(toString);
    when(mockModule.getIcon()).thenReturn(icon);
    when(mockModule.activate()).thenReturn(displayNode);
    when(mockModule.destroy()).thenReturn(true);
    when(mockModule.toString()).thenReturn(toString);
    return mockModule;
  }

}
