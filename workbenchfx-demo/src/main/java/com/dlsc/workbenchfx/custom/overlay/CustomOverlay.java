package com.dlsc.workbenchfx.custom.overlay;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CustomOverlay extends BorderPane {

  private WorkbenchFx workbench;
  private final boolean blocking;

  public CustomOverlay(WorkbenchFx workbench, boolean blocking) {
    this.workbench = workbench;
    this.blocking = blocking;
    init();
  }

  private void init() {
    getStyleClass().add("custom-overlay");

    Label centerLbl = new Label("This is an example of a custom overlay!");
    centerLbl.getStyleClass().add("centerLbl");
    setCenter(centerLbl);

    if (blocking) {
      // only show x button if it's a blocking overlay, so it can still be closed
      Button closeBtn = new Button("", new FontAwesomeIconView(FontAwesomeIcon.CLOSE));
      closeBtn.setOnAction(event -> workbench.hideOverlay(this, true));
      BorderPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
      setTop(closeBtn);
    }
  }

  @Override
  public String toString() {
    return "Custom Overlay - Blocking: " + blocking;
  }
}
