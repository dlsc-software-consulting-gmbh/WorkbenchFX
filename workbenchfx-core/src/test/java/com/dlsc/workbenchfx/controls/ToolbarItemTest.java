package com.dlsc.workbenchfx.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Tests for {@link ToolbarItem}.
 */
@Tag("fast")
class ToolbarItemTest extends ApplicationTest {

  private static final String TOOLBAR_BUTTON = "toolbar-button";
  private static final String TOOLBAR_LABEL = "toolbar-label";
  private static final String TOOLBAR_COMBO_BOX = "toolbar-menu-button";

  // ToolbarItem items
  private String toolbarItemText;
  private FontAwesomeIconView toolbarItemIconView;
  private ImageView toolbarItemImageView;
  private MenuItem toolbarItemMenuItem;
  private EventHandler<? super MouseEvent> toolbarItemOnClick;

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
    toolbarItemOnClick = event -> System.out.println("Item Clicked");
  }

  @Test
  void testCtors() {
    // Default ctor
    toolbarItem = new ToolbarItem();
    assertTrue(toolbarItem.getStyleClass().isEmpty());

    // Label ctors
    toolbarItem = new ToolbarItem(toolbarItemText);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    toolbarItem = new ToolbarItem(toolbarItemImageView);
    assertEquals(toolbarItemImageView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    // Button ctors
    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemOnClick);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemOnClick, toolbarItem.getOnClick());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_BUTTON));

    toolbarItem = new ToolbarItem(toolbarItemImageView, toolbarItemOnClick);
    assertEquals(toolbarItemImageView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_BUTTON));

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView, toolbarItemOnClick);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertEquals(toolbarItemOnClick, toolbarItem.getOnClick());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_BUTTON));

    // MenuButton ctors
    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemMenuItem);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertTrue(toolbarItem.getItems().contains(toolbarItemMenuItem));
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_COMBO_BOX));

    toolbarItem = new ToolbarItem(toolbarItemImageView, toolbarItemMenuItem);
    assertEquals(toolbarItemImageView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getItems().contains(toolbarItemMenuItem));
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_COMBO_BOX));

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView, toolbarItemMenuItem);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getItems().contains(toolbarItemMenuItem));
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_COMBO_BOX));
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
