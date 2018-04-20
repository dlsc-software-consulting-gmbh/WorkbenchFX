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
   * Checks if two Nodes are equal. This problem might happen when generating the Tabs and Tiles.
   *
   * @param node1 the first of the two Nodes to be compared
   * @param node2 the second of the two Nodes to be compared
   */
  public static void assertNodeNotSame(Node node1, Node node2) {
    if (node1 == node2) {
      throw new IllegalArgumentException("Node can only have one parent. "
          + "Please use two different Node object instances.");
    }
  }

}
