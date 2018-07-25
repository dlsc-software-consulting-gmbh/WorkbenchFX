package com.dlsc.workbenchfx.custom.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by François Martin on 24.07.18.
 */
public class CalendarDrawer extends VBox {

  HBox userBox = new HBox();
  FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
  Label userLbl = new Label("workbenchfx@dlsc.com");

  GridPane calendarGrid = new GridPane();
  Rectangle workRect = new Rectangle(20, 20, Color.RED);
  Label workLbl = new Label("Work");

  public CalendarDrawer() {
    userBox.getChildren().addAll(userIcon, userLbl);

    calendarGrid.add(workRect, 0, 0);
    calendarGrid.add(workLbl, 1, 0);

    getChildren().addAll(userBox, calendarGrid);
  }

}
