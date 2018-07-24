package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.custom.controls.CustomDrawer;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Created by François Martin on 24.07.18.
 */
public class DrawerTestModule extends WorkbenchModule {
  private final Button leftBtn = new Button("Left Drawer, full");
  private final Button rightBtn = new Button("Right Drawer, full");
  private final Button topBtn = new Button("Top Drawer, full");
  private final Button bottomBtn = new Button("Bottom Drawer, full");

  private final Button leftPercentBtn = new Button("Left Drawer, 33%");
  private final Button rightPercentBtn = new Button("Right Drawer, 33%");
  private final Button topPercentBtn = new Button("Top Drawer, 33%");
  private final Button bottomPercentBtn = new Button("Bottom Drawer, 33%");

  private final GridPane customPane = new GridPane();

  public DrawerTestModule() {
    super("Drawer Test", FontAwesomeIcon.QUESTION);
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    customPane.add(topBtn, 2, 1);
    customPane.add(rightBtn, 3, 2);
    customPane.add(bottomBtn, 2, 3);
    customPane.add(leftBtn, 1, 2);

    customPane.add(topPercentBtn, 6, 1);
    customPane.add(rightPercentBtn, 7, 2);
    customPane.add(bottomPercentBtn, 6, 3);
    customPane.add(leftPercentBtn, 5, 2);

    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    leftBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.LEFT));
    rightBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.RIGHT));
    topBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.TOP));
    bottomBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.BOTTOM));

    leftPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.LEFT, 33));
    rightPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.RIGHT, 33));
    topPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.TOP, 33));
    bottomPercentBtn.setOnAction(event -> getWorkbench().showDrawer(new CustomDrawer(), Side.BOTTOM, 33));
  }

  @Override
  public Node activate() {
    return customPane;
  }

}
