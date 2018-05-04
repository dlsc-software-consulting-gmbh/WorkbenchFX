package com.dlsc.workbenchfx.custom.overlay;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.overlay.Overlay;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CustomOverlay extends BorderPane implements Overlay {

  private WorkbenchFx workbench;
  private final boolean blocking;

  public CustomOverlay(boolean blocking) {
    this.blocking = blocking;
    init();
  }

  private void init() {
    getStyleClass().add("custom-overlay");

    Label centerLbl = new Label("This is an example of a custom overlay!");
    centerLbl.getStyleClass().add("centerLbl");
    setCenter(centerLbl);
  }

  @Override
  public String toString() {
    return "Custom Overlay - Blocking: " + blocking;
  }

  @Override
  public void init(WorkbenchFx workbench) {
    this.workbench = workbench;
    // TODO: change to blocking
    if (blocking) {
      // only show x button if it's a blocking overlay, so it can still be closed
      Button closeBtn = new Button("", new FontAwesomeIconView(FontAwesomeIcon.CLOSE));
      closeBtn.setOnAction(event -> setVisible(false));
      BorderPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
      setTop(closeBtn);
    }
  }

  @Override
  public boolean isBlocking() {
    return blocking;
  }

  @Override
  public Node getNode() {
    return this;
  }
}
