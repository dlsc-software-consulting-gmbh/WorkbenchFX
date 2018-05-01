package com.dlsc.workbenchfx.custom.overlay;

import com.dlsc.workbenchfx.WorkbenchFx;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;

public class CustomOverlay extends BorderPane {

  private final WorkbenchFx workbench;
  private final boolean modal;

  public CustomOverlay(WorkbenchFx workbench, boolean modal) {
    this.workbench = workbench;
    this.modal = modal;
    init();
  }

  private void init() {
    getStyleClass().add("custom-overlay");

    Label centerLbl = new Label("This is an example of a custom overlay!");
    centerLbl.getStyleClass().add("centerLbl");
    setCenter(centerLbl);

    if (!modal) {
      // only show x button if it's not a modal overlay
      Button closeBtn = new Button("", new FontAwesomeIconView(FontAwesomeIcon.CLOSE));
      BorderPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
      closeBtn.setOnAction(event -> workbench.hideOverlay(this, false));
      setTop(closeBtn);
    }
  }

  @Override
  public String toString() {
    return "Custom Overlay - Modal: " + modal;
  }

}
