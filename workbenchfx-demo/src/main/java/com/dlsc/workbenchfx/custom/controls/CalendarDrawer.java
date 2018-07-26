package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.Workbench;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by François Martin on 24.07.18.
 */
public class CalendarDrawer extends VBox {

  private static final int RECT_SIZE = 20;
  private final Workbench workbench;
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

  GridPane drawerGrid = new GridPane();
  private final Button calendarLeftPercentBtn = new Button("Left Drawer, 33%");
  private final Button calendarRightPercentBtn = new Button("Right Drawer, 33%");
  private final Button calendarTopPercentBtn = new Button("Top Drawer, 33%");
  private final Button calendarBottomPercentBtn = new Button("Bottom Drawer, 33%");
  private final Button hideBtn = new Button("Hide");

  public CalendarDrawer(Workbench workbench) {
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    this.workbench = workbench;
    calendarGrid.setAlignment(Pos.CENTER);
    drawerGrid.setAlignment(Pos.CENTER);
    userBox.setAlignment(Pos.CENTER);

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
    calendarGrid.add(friendsLbl, 1, 3);

    calendarGrid.getChildren().forEach(node -> {
      GridPane.setMargin(node, new Insets(10));
    });

    drawerGrid.add(calendarTopPercentBtn, 2, 5);
    drawerGrid.add(calendarRightPercentBtn, 3, 6);
    drawerGrid.add(calendarBottomPercentBtn, 2, 7);
    drawerGrid.add(calendarLeftPercentBtn, 1, 6);
    drawerGrid.add(hideBtn, 0, 8);

    setAlignment(Pos.CENTER);

    getChildren().addAll(userBox, calendarGrid, drawerGrid);

    setPadding(new Insets(20));
  }

  private void setupEventHandlers() {
    calendarLeftPercentBtn.setOnAction(event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.LEFT, 33));
    calendarRightPercentBtn.setOnAction(event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.RIGHT, 33));
    calendarTopPercentBtn.setOnAction(event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.TOP, 33));
    calendarBottomPercentBtn.setOnAction(event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.BOTTOM, 33));
    hideBtn.setOnAction(event -> workbench.hideDrawer());
  }

}
