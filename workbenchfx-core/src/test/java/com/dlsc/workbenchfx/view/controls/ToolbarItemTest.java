package com.dlsc.workbenchfx.view.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
  private FontIcon toolbarItemIconView;
  private MenuItem toolbarItemMenuItem;
  private EventHandler<? super MouseEvent> toolbarItemOnClick;

  private ToolbarItem toolbarItem;

  @BeforeEach
  void setup() {
    // Initialization of items for ToolbarItem testing
    toolbarItemText = "ToolbarItem Text";
    toolbarItemIconView = new FontIcon(MaterialDesign.MDI_ACCOUNT);
    toolbarItemMenuItem = new MenuItem("Menu Item");
    toolbarItemOnClick = event -> System.out.println("Item Clicked");
  }

  @Test
  void testCtors() {
    // Default ctor
    toolbarItem = new ToolbarItem();
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    // Label ctors
    toolbarItem = new ToolbarItem(toolbarItemText);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    toolbarItem = new ToolbarItem(toolbarItemIconView);
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
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

    toolbarItem = new ToolbarItem(toolbarItemIconView, toolbarItemOnClick);
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
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

    toolbarItem = new ToolbarItem(toolbarItemIconView, toolbarItemMenuItem);
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getItems().contains(toolbarItemMenuItem));
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_COMBO_BOX));

    toolbarItem = new ToolbarItem(toolbarItemText, toolbarItemIconView, toolbarItemMenuItem);
    assertEquals(toolbarItemText, toolbarItem.getText());
    assertEquals(toolbarItemIconView, toolbarItem.getGraphic());
    assertTrue(toolbarItem.getItems().contains(toolbarItemMenuItem));
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_COMBO_BOX));
  }

  @Test
  void testOnClickListener() {
    // Creating the Item and everything should be default
    toolbarItem = new ToolbarItem();
    assertNull(toolbarItem.getOnMouseClicked());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    // Setting the onclick event
    toolbarItem.setOnClick(toolbarItemOnClick);

    // The onMouseClicked event should be set and the style class also
    assertEquals(toolbarItemOnClick, toolbarItem.getOnMouseClicked());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_BUTTON));

    // Setting the onclick event null
    toolbarItem.setOnClick(null);

    // The onMouseClicked event should be removed and the style class should now be a label
    assertNull(toolbarItem.getOnMouseClicked());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));
  }

  @Test
  void testItemsListener() {
    // Creating the Item and everything should be default
    toolbarItem = new ToolbarItem();
    assertTrue(toolbarItem.getItems().isEmpty());
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));

    // Setting the item
    toolbarItem.getItems().add(toolbarItemMenuItem);

    // The style class should be set
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_COMBO_BOX));

    // Clear items
    toolbarItem.getItems().clear();

    // The the style class should now be a label
    assertTrue(toolbarItem.getStyleClass().contains(TOOLBAR_LABEL));
  }

  @Test
  void testGraphicListenerDefaultCtor() {
    // create a new image 20x20
    DoubleProperty fitHeightProperty = new SimpleDoubleProperty();
    ImageView imageView = mock(ImageView.class);
    when(imageView.fitHeightProperty()).thenReturn(fitHeightProperty);

    // init a new empty ToolbarItem and set the ImageView
    toolbarItem = new ToolbarItem();
    toolbarItem.setGraphic(imageView);
    toolbarItem.setPrefHeight(100);

    // expected outcome: fitheight to 47 (due to factor 0.47) and preserveratio is set
    assertEquals(47, fitHeightProperty.get(), .1);
    verify(imageView).setPreserveRatio(true);
    verify(imageView).fitHeightProperty();
  }

  @Test
  void testGraphicListenerUsingCtor() {
    // create a new image 20x20
    DoubleProperty fitHeightProperty = new SimpleDoubleProperty();
    ImageView imageView = mock(ImageView.class);
    when(imageView.fitHeightProperty()).thenReturn(fitHeightProperty);

    // init a new ToolbarItem with the ImageView (to test if the listener triggers)
    toolbarItem = new ToolbarItem(imageView);
    toolbarItem.setPrefHeight(100);

    // expected outcome: fitheight to 47 (due to factor 0.47) and preserveratio is set
    assertEquals(47, fitHeightProperty.get(), .1);
    verify(imageView).setPreserveRatio(true);
    verify(imageView).fitHeightProperty();
  }
}
