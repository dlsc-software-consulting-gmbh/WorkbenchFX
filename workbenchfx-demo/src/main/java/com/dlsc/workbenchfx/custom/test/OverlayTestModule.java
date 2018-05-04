package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.custom.overlay.AnotherCustomOverlay;
import com.dlsc.workbenchfx.custom.overlay.CustomOverlay;
import com.dlsc.workbenchfx.module.AbstractModule;
import com.dlsc.workbenchfx.overlay.Overlay;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class OverlayTestModule extends AbstractModule {
  private final Button addOverlayBtn = new Button("Add new Overlay");
  private final Button showOverlayBtn = new Button("Show new Overlay");
  private final Button removeOverlayBtn = new Button("Remove new Overlay");
  private final Button hideOverlayBtn = new Button("Hide new Overlay");

  private final Button addBlockingOverlayBtn = new Button("Add new blocking Overlay");
  private final Button showBlockingOverlayBtn = new Button("Show new blocking Overlay");
  private final Button removeBlockingOverlayBtn = new Button("Remove new blocking Overlay");
  private final Button hideBlockingOverlayBtn = new Button("Hide new blocking Overlay");
  
  private final Overlay overlay = new AnotherCustomOverlay(false);
  private final Overlay blockingOverlay = new AnotherCustomOverlay(true);

  private final GridPane customPane = new GridPane();

  public OverlayTestModule() {
    super("Overlay Test", FontAwesomeIcon.QUESTION);
    layoutParts();
    setupEventHandlers();
  }

  private void layoutParts() {
    customPane.add(addOverlayBtn,    0, 0);
    customPane.add(showOverlayBtn,   0, 1);
    customPane.add(removeOverlayBtn, 1, 0);
    customPane.add(hideOverlayBtn,   1, 1);

    customPane.add(addBlockingOverlayBtn,    0, 2);
    customPane.add(showBlockingOverlayBtn,   0, 3);
    customPane.add(removeBlockingOverlayBtn, 1, 2);
    customPane.add(hideBlockingOverlayBtn,   1, 3);

    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    addOverlayBtn.setOnAction(event -> workbench.addOverlay(overlay));
    showOverlayBtn.setOnAction(event -> overlay.getNode().setVisible(true));
    removeOverlayBtn.setOnAction(event -> workbench.removeOverlay(overlay));
    hideOverlayBtn.setOnAction(event -> overlay.getNode().setVisible(false));

    addBlockingOverlayBtn.setOnAction(event -> workbench.addOverlay(blockingOverlay));
    showBlockingOverlayBtn.setOnAction(event -> blockingOverlay.getNode().setVisible(true));
    removeBlockingOverlayBtn.setOnAction(event -> workbench.removeOverlay(blockingOverlay));
    hideBlockingOverlayBtn.setOnAction(event -> blockingOverlay.getNode().setVisible(false));
  }

  @Override
  public Node activate() {
    return customPane;
  }
}
