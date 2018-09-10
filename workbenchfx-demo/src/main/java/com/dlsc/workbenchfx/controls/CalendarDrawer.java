package com.dlsc.workbenchfx.controls;

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
  private final Button calendarLeftBtn = new Button("Left Drawer, full");
  private final Button calendarRightBtn = new Button("Right Drawer, full");
  private final Button calendarTopBtn = new Button("Top Drawer, full");
  private final Button calendarBottomBtn = new Button("Bottom Drawer, full");
  private final Button hideBtn = new Button("Hide");

  public CalendarDrawer(Workbench workbench) {
    this.workbench = workbench;
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    calendarGrid.setAlignment(Pos.CENTER);
    drawerGrid.setAlignment(Pos.CENTER);
    userBox.setAlignment(Pos.CENTER);

    userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_CIRCLE);
    userIcon.setId("user-icon");
    userIcon.setStyle(
        "-fx-fill: black; -fx-font-family: FontAwesome; -fx-font-size: 2em !important;");
    userBox.getChildren().addAll(userIcon, userLbl);
    userBox.setMargin(userIcon, new Insets(10));

    calendarGrid.add(workRect, 0, 0);
    calendarGrid.add(workLbl, 1, 0);

    calendarGrid.add(homeRect, 0, 1);
    calendarGrid.add(homeLbl, 1, 1);

    calendarGrid.add(familyRect, 0, 2);
    calendarGrid.add(familyLbl, 1, 2);

    calendarGrid.add(friendsRect, 0, 3);
    calendarGrid.add(friendsLbl, 1, 3);

    calendarGrid.getChildren().forEach(node -> {
      GridPane.setMargin(node, new Insets(10));
    });

    drawerGrid.add(calendarTopBtn, 2, 5);
    drawerGrid.add(calendarRightBtn, 3, 6);
    drawerGrid.add(calendarBottomBtn, 2, 7);
    drawerGrid.add(calendarLeftBtn, 1, 6);
    drawerGrid.add(hideBtn, 2, 8);

    drawerGrid.getChildren().forEach(node -> {
      GridPane.setMargin(node, new Insets(5));
      Button button = (Button) node;
      int width = 150;
      int height = 30;
      button.setMaxSize(width, height);
      button.setPrefSize(width, height);
    });

    setAlignment(Pos.CENTER);

    getChildren().addAll(userBox, calendarGrid, drawerGrid);

    setPadding(new Insets(20));
  }

  private void setupEventHandlers() {
    calendarLeftBtn.setOnAction(
        event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.LEFT));
    calendarRightBtn.setOnAction(
        event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.RIGHT));
    calendarTopBtn.setOnAction(
        event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.TOP));
    calendarBottomBtn.setOnAction(
        event -> workbench.showDrawer(new CalendarDrawer(workbench), Side.BOTTOM));
    hideBtn.setOnAction(event -> workbench.hideDrawer());
  }

}
