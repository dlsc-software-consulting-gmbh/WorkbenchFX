package com.dlsc.workbenchfx.util;

import javafx.scene.Node;

/**
 * Provides utility methods to do general transformations between different model objects of
 * WorkbenchFX and or lists and maps of them.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxUtils {

  /**
   * Makes sure the addresses of two nodes aren't the same.
   * Is used to check {@link Node}s passed in by an API user. Since every {@link Node} can only be
   * associated with one parent, if two {@link Node}s are being passed in, there is a certain risk
   * the API user may pass in the same node twice, which will result in the node only being rendered
   * in the GUI once.
   *
   * @param node1 first node to check
   * @param node2 second node to check
   * @throws IllegalArgumentException if node1 == node2
   */
  public static void assertNodeNotSame(Node node1, Node node2) {
    if (node1 == node2) {
      throw new IllegalArgumentException(
          "Node can only have one parent. Please use two different Node object instances.");
    }
  }
}
