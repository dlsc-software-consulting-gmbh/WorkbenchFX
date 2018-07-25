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

  private static final int RECT_SIZE = 20;
  HBox userBox = new HBox();
  FontAwesomeIconView userIcon;
  Label userLbl = new Label("workbenchfx@dlsc.com");

  GridPane calendarGrid = new GridPane();
  Rectangle workRect = new Rectangle(RECT_SIZE, RECT_SIZE, Color.RED);
  Label workLbl = new Label("Work");
  Rectangle homeRect = new Rectangle(RECT_SIZE, RECT_SIZE, Color.ORANGE);
  Label homeLbl = new Label("Home");
  Rectangle familyRect = new Rectangle(RECT_SIZE, RECT_SIZE, Color.YELLOW);
  Label familyLbl = new Label("Family");
  Rectangle friendsRect = new Rectangle(RECT_SIZE, RECT_SIZE, Color.GREEN);
  Label friendsLbl = new Label("Friends");

  public CalendarDrawer() {
    //setAlignment(Pos.CENTER);

    userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_CIRCLE);
    userIcon.setStyle("-fx-fill: black");
    userBox.getChildren().addAll(userIcon, userLbl);

    calendarGrid.add(workRect, 0, 0);
    calendarGrid.add(workLbl,  1, 0);

    calendarGrid.add(homeRect, 0, 1);
    calendarGrid.add(homeLbl,  1, 1);

    calendarGrid.add(familyRect, 0, 2);
    calendarGrid.add(familyLbl,  1, 2);

    calendarGrid.add(friendsRect, 0, 3);
    calendarGrid.add(friendsLbl,  1, 3);


    calendarGrid.getChildren().forEach(node -> {
          GridPane.setMargin(node, new Insets(10));
        });

    getChildren().addAll(userBox, calendarGrid);
  }

}
