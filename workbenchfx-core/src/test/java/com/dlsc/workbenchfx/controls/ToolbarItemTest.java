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
  private String toolbarItemText;
  private FontAwesomeIconView toolbarItemIconView;
  private ImageView toolbarItemImageView;
  private MenuItem toolbarItemMenuItem;

  private ToolbarItem toolbarItem;

  @BeforeEach
  void setup() {
    // Initialization of items for ToolbarItem testing
    toolbarItemText = "ToolbarItem Text";
    toolbarItemIconView = new FontAwesomeIconView(FontAwesomeIcon.QUESTION);
    toolbarItemImageView = new ImageView(
        new Image(ToolbarItemTest.class.getResource("../date-picker.png").toExternalForm())
    );
    toolbarItemMenuItem = new MenuItem("Menu Item");

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView, toolbarItemMenuItem);
  }

  @Test
  void createToolbarItem() {
    toolbarItem = new ToolbarItem(toolbarItemText);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertNull(toolbarItem.getGraphic());
    assertSame(0, toolbarItem.getItems().size());

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemMenuItem);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertNull(toolbarItem.getGraphic());
    assertSame(1, toolbarItem.getItems().size());

    toolbarItem = new ToolbarItem(toolbarItemIconView);
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertNull(toolbarItem.getText());
    assertSame(0, toolbarItem.getItems().size());

    toolbarItem = new ToolbarItem(toolbarItemIconView, toolbarItemMenuItem);
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertNull(toolbarItem.getText());
    assertSame(1, toolbarItem.getItems().size());

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertSame(0, toolbarItem.getItems().size());

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView, toolbarItemMenuItem);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertSame(1, toolbarItem.getItems().size());

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemImageView, toolbarItemMenuItem);
    assertEquals(toolbarItemImageView, toolbarItem.getGraphic());
  }

  @Test
  void removeItemFromToolbarItem() {
    int initialSize = toolbarItem.getItems().size();
    toolbarItem.getItems().remove(0);
    assertSame(initialSize - 1, toolbarItem.getItems().size());
  }

  @Test
  void addItemFromToolbarItem() {
    int initialSize = toolbarItem.getItems().size();
    toolbarItem.getItems().add(new CustomMenuItem(new Label("New Item")));
    assertSame(initialSize + 1, toolbarItem.getItems().size());
  }
}
