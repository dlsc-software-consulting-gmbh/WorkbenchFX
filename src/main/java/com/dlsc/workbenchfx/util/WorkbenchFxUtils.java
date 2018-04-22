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

  public static void assertNodeNotSame(Node node1, Node node2) {
    if (node1 == node2) {
      throw new IllegalArgumentException("Node can only have one parent. Please use two different Node object instances.");
    }
  }

}
