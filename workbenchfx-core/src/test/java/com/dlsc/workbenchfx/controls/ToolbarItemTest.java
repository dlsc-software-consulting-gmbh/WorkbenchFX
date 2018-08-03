package com.dlsc.workbenchfx.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Tests for {@link ToolbarItem}.
 */
@Tag("fast")
class ToolbarItemTest extends ApplicationTest {
  // ToolbarItem items
  private String dropdownText;
  private FontAwesomeIconView dropdownIconView;
  private ImageView dropdownImageView;
  private MenuItem dropdownMenuItem;

  private ToolbarItem toolbarItem;

  @BeforeEach
  void setup() {
    // Initialization of items for ToolbarItem testing
    dropdownText = "ToolbarItem Text";
    dropdownIconView = new FontAwesomeIconView(FontAwesomeIcon.QUESTION);
    dropdownImageView = new ImageView(
        new Image(ToolbarItemTest.class.getResource("../date-picker.png").toExternalForm())
    );
    dropdownMenuItem = new MenuItem("Menu Item");

    toolbarItem = ToolbarItem.of(dropdownText, dropdownIconView, dropdownMenuItem);
  }

  @Test
  void createDropdown() {
    toolbarItem = ToolbarItem.of(dropdownText);
    assertEquals(dropdownText, toolbarItem.getText());
    assertNull(toolbarItem.getIcon());
    assertSame(0, toolbarItem.getItems().size());

    toolbarItem = ToolbarItem.of(dropdownText, dropdownMenuItem);
    assertEquals(dropdownText, toolbarItem.getText());
    assertNull(toolbarItem.getIcon());
    assertSame(1, toolbarItem.getItems().size());

    toolbarItem = ToolbarItem.of(dropdownIconView);
    assertEquals(dropdownIconView, toolbarItem.getIcon());
    assertNull(toolbarItem.getText());
    assertSame(0, toolbarItem.getItems().size());

    toolbarItem = ToolbarItem.of(dropdownIconView, dropdownMenuItem);
    assertEquals(dropdownIconView, toolbarItem.getIcon());
    assertNull(toolbarItem.getText());
    assertSame(1, toolbarItem.getItems().size());

    toolbarItem = ToolbarItem.of(dropdownText, dropdownIconView);
    assertEquals(dropdownText, toolbarItem.getText());
    assertEquals(dropdownIconView, toolbarItem.getIcon());
    assertSame(0, toolbarItem.getItems().size());

    toolbarItem = ToolbarItem.of(dropdownText, dropdownIconView, dropdownMenuItem);
    assertEquals(dropdownText, toolbarItem.getText());
    assertEquals(dropdownIconView, toolbarItem.getIcon());
    assertSame(1, toolbarItem.getItems().size());

    toolbarItem = ToolbarItem.of(dropdownText, dropdownImageView, dropdownMenuItem);
    assertEquals(dropdownImageView, toolbarItem.getIcon());
  }

  @Test
  void removeItemFromDropdown() {
    int initialSize = toolbarItem.getItems().size();
    toolbarItem.getItems().remove(0);
    assertSame(initialSize - 1, toolbarItem.getItems().size());
  }

  @Test
  void addItemFromDropdown() {
    int initialSize = toolbarItem.getItems().size();
    toolbarItem.getItems().add(new CustomMenuItem(new Label("New Item")));
    assertSame(initialSize + 1, toolbarItem.getItems().size());
  }
}
