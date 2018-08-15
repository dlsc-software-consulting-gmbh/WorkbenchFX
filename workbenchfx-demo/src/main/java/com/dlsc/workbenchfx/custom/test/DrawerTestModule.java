package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.custom.controls.CalendarDrawer;
import com.dlsc.workbenchfx.custom.controls.MapDrawer;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 24.07.18.
 */
public class DrawerTestModule extends WorkbenchModule {
  // Map
  private final Button leftBtn = new Button("Left Drawer, full");
  private final Button rightBtn = new Button("Right Drawer, full");
  private final Button topBtn = new Button("Top Drawer, full");
  private final Button bottomBtn = new Button("Bottom Drawer, full");

  private final Button leftPercentBtn = new Button("Left Drawer, 33%");
  private final Button rightPercentBtn = new Button("Right Drawer, 33%");
  private final Button topPercentBtn = new Button("Top Drawer, 33%");
  private final Button bottomPercentBtn = new Button("Bottom Drawer, 33%");

  // Calendar
  private final Button calendarLeftBtn = new Button("Left Drawer, full");
  private final Button calendarRightBtn = new Button("Right Drawer, full");
  private final Button calendarTopBtn = new Button("Top Drawer, full");
  private final Button calendarBottomBtn = new Button("Bottom Drawer, full");

  private final Button calendarLeftPercentBtn = new Button("Left Drawer, 33%");
  private final Button calendarRightPercentBtn = new Button("Right Drawer, 33%");
  private final Button calendarTopPercentBtn = new Button("Top Drawer, 33%");
  private final Button calendarBottomPercentBtn = new Button("Bottom Drawer, 33%");

  private final GridPane mapPane = new GridPane();
  private final Label mapLbl = new Label("Map Drawer");
  private final GridPane calendarPane = new GridPane();
  private final Label calendarLbl = new Label("Calendar Drawer");

  private final VBox contentBox = new VBox(mapLbl, mapPane, calendarLbl, calendarPane);

  public DrawerTestModule() {
    super("Drawer Test", MaterialDesignIcon.HELP);
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    mapPane.add(topBtn, 2, 1);
    mapPane.add(rightBtn, 3, 2);
    mapPane.add(bottomBtn, 2, 3);
    mapPane.add(leftBtn, 1, 2);

    mapPane.add(topPercentBtn, 6, 1);
    mapPane.add(rightPercentBtn, 7, 2);
    mapPane.add(bottomPercentBtn, 6, 3);
    mapPane.add(leftPercentBtn, 5, 2);


    calendarPane.add(calendarTopBtn, 2, 1);
    calendarPane.add(calendarRightBtn, 3, 2);
    calendarPane.add(calendarBottomBtn, 2, 3);
    calendarPane.add(calendarLeftBtn, 1, 2);

    calendarPane.add(calendarTopPercentBtn, 6, 1);
    calendarPane.add(calendarRightPercentBtn, 7, 2);
    calendarPane.add(calendarBottomPercentBtn, 6, 3);
    calendarPane.add(calendarLeftPercentBtn, 5, 2);

    mapPane.setAlignment(Pos.CENTER);
    calendarPane.setAlignment(Pos.CENTER);
    contentBox.setAlignment(Pos.CENTER);
    contentBox.setSpacing(20);
  }

  private void setupEventHandlers() {
    // Map
    MapDrawer drawer = new MapDrawer();
    leftBtn.setOnAction(event -> {
      getWorkbench().showDrawer(drawer, Side.LEFT);
    });
    rightBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.RIGHT));
    topBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.TOP));
    bottomBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.BOTTOM));

    leftPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.LEFT, 33));
    rightPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.RIGHT, 33));
    topPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.TOP, 33));
    bottomPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new MapDrawer(), Side.BOTTOM, 33));

    // Calendar
    calendarLeftBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.LEFT));
    calendarRightBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.RIGHT));
    calendarTopBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.TOP));
    calendarBottomBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.BOTTOM));

    calendarLeftPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.LEFT, 33));
    calendarRightPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.RIGHT, 33));
    calendarTopPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.TOP, 33));
    calendarBottomPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CalendarDrawer(getWorkbench()), Side.BOTTOM, 33));
  }

  @Override
  public Node activate() {
    return contentBox;
  }

}
