package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class HomeView extends StackPane implements View {
  private final WorkbenchFx model;
  HBox tileBox;

  /**
   * Creates a new {@link HomeView}.
   */
  public HomeView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("home");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    tileBox = new HBox();
    tileBox.setId("tileBox");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    getChildren().add(tileBox);
  }

  public void addTile(Node tile) {
    tileBox.getChildren().add(tile);
  }
}
