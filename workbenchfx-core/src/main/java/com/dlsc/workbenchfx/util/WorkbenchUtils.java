package com.dlsc.workbenchfx.util;

import com.google.common.base.CharMatcher;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;

/**
 * Provides utility methods to do general transformations between different model objects of
 * WorkbenchFX and or lists and maps of them.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class WorkbenchUtils {

  /**
   * Utility class should not be possible to be instantiated.
   */
  private WorkbenchUtils() {

  }

  /**
   * Makes sure the addresses of two nodes aren't the same. Is used to check {@link Node}s passed in
   * by an API user. Since every {@link Node} can only be associated with one parent, if two {@link
   * Node}s are being passed in, there is a certain risk the API user may pass in the same node
   * twice, which will result in the node only being rendered in the GUI once.
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

  /**
   * Adds a {@link SetChangeListener} to an {@link ObservableSet}.
   *
   * @param set to add a listener to
   * @param added action to be performed when an object was added to the {@link Set}
   * @param removed action to be performed when an object was removed from the {@link Set}
   * @param <T> type of the {@link ObservableSet}
   */
  public static <T> void addSetListener(ObservableSet<T> set,
                                        Consumer<SetChangeListener.Change<? extends T>> added,
                                        Consumer<SetChangeListener.Change<? extends T>> removed) {
    set.addListener((SetChangeListener<? super T>) c -> {
      if (c.wasAdded()) {
        added.accept(c);
      } else if (c.wasRemoved()) {
        removed.accept(c);
      }
    });
  }

  /**
   * Converts a given String into one that can be used in css.
   * Accepts only characters that match 'a' to 'z', 'A' to 'Z', '0' to '9', ' ' and '-'.
   * All uppercase letters are converted into a lowercase and spaces are replaced by '-'.
   *
   * @param name the {@link String} to be converted into an css id
   * @return     the converted {@link String} with only the allowed characters,
   *             characters in lowercase and whitespaces replaced by '-'
   */
  public static String convertToId(String name) {
    return CharMatcher.inRange('a', 'z')
        .or(CharMatcher.inRange('A', 'Z'))
        .or(CharMatcher.inRange('0', '9'))
        .or(CharMatcher.is(' '))
        .or(CharMatcher.is('-'))
        .retainFrom(name)
        .replace(' ', '-')
        .toLowerCase();
  }

  /**
   * Calculates from the amount of modules per page how many columns of modules there should be
   * in a row.
   *
   * @param modulesPerPage how many modules are shown in a page, <b>maximum of 12</b> to get
   *                       reliable calculations
   * @return the amount of columns per row
   */
  public static int calculateColumnsPerRow(int modulesPerPage) {
    if (modulesPerPage <= 3) {
      return modulesPerPage;
    }
    if (divisibleBy(modulesPerPage, 2)) {
      return modulesPerPage / 2;
    }
    if (divisibleBy(modulesPerPage, 3)) {
      return modulesPerPage / 3;
    }
    if (modulesPerPage < 10) {
      if (divisibleBy(modulesPerPage + 1, 2)) {
        return (modulesPerPage + 1) / 2;
      }
      if (divisibleBy(modulesPerPage + 1, 3)) {
        return (modulesPerPage + 1) / 3;
      }
    } else {
      if (divisibleBy(modulesPerPage + 1, 3)) {
        return (modulesPerPage + 1) / 3;
      }
      if (divisibleBy(modulesPerPage + 1, 2)) {
        return (modulesPerPage + 1) / 2;
      }
    }
    return (modulesPerPage / 4) + 1;
  }

  /**
   * Checks if a is divisible by b
   * @param a number
   * @param b number
   * @return true, if a is divisible by b without remainder
   */
  public static boolean divisibleBy(int a, int b) {
    return a % b == 0;
  }
}
