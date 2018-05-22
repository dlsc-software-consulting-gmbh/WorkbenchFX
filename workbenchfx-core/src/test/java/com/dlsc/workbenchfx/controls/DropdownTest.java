package com.dlsc.workbenchfx.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlsc.workbenchfx.view.controls.Dropdown;
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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Created by François Martin on 20.03.18.
 */
@Tag("fast")
class DropdownTest extends ApplicationTest {

  private FxRobot robot;

  // Dropdown items
  private String dropdownText;
  private FontAwesomeIconView dropdownIconView;
  private ImageView dropdownImageView;
  private MenuItem dropdownMenuItem;

  private Dropdown d;

  @BeforeEach
  void setup() {
    // Initialization of items for Dropdown testing
    dropdownText = "Dropdown Text";
    dropdownIconView = new FontAwesomeIconView(FontAwesomeIcon.QUESTION);
    dropdownImageView = new ImageView(new Image("http://www.cherriz.de/training/content/images/oberflaechen_in_java/Javafx_logo_color.png"));
    dropdownMenuItem = new MenuItem("Menu Item");

    d = Dropdown.of(dropdownText, dropdownIconView, dropdownMenuItem);
  }

  @Test
  void createDropdown() {
    robot.interact(() -> {
      d = Dropdown.of(dropdownText);
      assertEquals(dropdownText, d.getText());
      assertNull(d.getIcon());
      assertEquals(0, d.getItems().size());

      d = Dropdown.of(dropdownText, dropdownMenuItem);
      assertEquals(dropdownText, d.getText());
      assertNull(d.getIcon());
      assertEquals(1, d.getItems().size());

      d = Dropdown.of(dropdownIconView);
      assertEquals(dropdownIconView, d.getIcon());
      assertNull(d.getText());
      assertEquals(0, d.getItems().size());

      d = Dropdown.of(dropdownIconView, dropdownMenuItem);
      assertEquals(dropdownIconView, d.getIcon());
      assertNull(d.getText());
      assertEquals(1, d.getItems().size());

      d = Dropdown.of(dropdownText, dropdownIconView);
      assertEquals(dropdownText, d.getText());
      assertEquals(dropdownIconView, d.getIcon());
      assertEquals(0, d.getItems().size());

      d = Dropdown.of(dropdownText, dropdownIconView, dropdownMenuItem);
      assertEquals(dropdownText, d.getText());
      assertEquals(dropdownIconView, d.getIcon());
      assertEquals(1, d.getItems().size());

      d = Dropdown.of(dropdownText, dropdownImageView, dropdownMenuItem);
      assertEquals(dropdownImageView, d.getIcon());
    });
  }

  @Test
  void invertDropdown() {
    robot.interact(() -> {
      assertFalse(d.getInverted());
      d.invertStyle();
      assertTrue(d.getInverted());
      d.invertStyle();
      assertFalse(d.invertedProperty().get());
    });
  }

  @Test
  void removeItemFromDropdown() {
    robot.interact(() -> {
      assertEquals(1, d.getItems().size());
      d.getItems().remove(0);
      assertEquals(0, d.getItems().size());
      d.getItems().add(new CustomMenuItem(new Label("New Item")));
      assertEquals(1, d.getItems().size());
    });
  }

  @Test
  void addItemFromDropdown() {
    robot.interact(() -> {
      assertEquals(1, d.getItems().size());
      d.getItems().remove(0);
      assertEquals(0, d.getItems().size());
      d.getItems().add(new CustomMenuItem(new Label("New Item")));
      assertEquals(1, d.getItems().size());
    });
  }
}
