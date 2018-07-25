package com.dlsc.workbenchfx.custom.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
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
  FontAwesomeIconView userIcon;
  Label userLbl = new Label("workbenchfx@dlsc.com");

  GridPane calendarGrid = new GridPane();
  Rectangle workRect = new Rectangle(20, 20, Color.RED);
  Label workLbl = new Label("Work");

  public CalendarDrawer() {
    userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_CIRCLE);
    userBox.getChildren().addAll(userIcon, userLbl);
    userIcon.setStyle("-fx-fill: black");

    calendarGrid.add(workRect, 0, 0);
    calendarGrid.add(workLbl, 1, 0);

    calendarGrid.getChildren().forEach(node -> {
          GridPane.setMargin(node, new Insets(10));
        });

    getChildren().addAll(userBox, calendarGrid);
  }

}
