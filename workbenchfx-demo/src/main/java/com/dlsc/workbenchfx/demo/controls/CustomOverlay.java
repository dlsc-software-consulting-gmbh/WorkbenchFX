package com.dlsc.workbenchfx.demo.controls;

import com.dlsc.workbenchfx.Workbench;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

public class CustomOverlay extends BorderPane {

  private Workbench workbench;
  private final boolean blocking;

  public CustomOverlay(Workbench workbench, boolean blocking) {
    StackPane.setAlignment(this, Pos.CENTER);
    Objects.requireNonNull(workbench);
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
      Button closeBtn = new Button("", new FontIcon(FontAwesome.CLOSE));
      closeBtn.setOnAction(event -> workbench.hideOverlay(this));
      BorderPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
      setTop(closeBtn);
    }
  }

  @Override
  public String toString() {
    return "Custom Overlay - Blocking: " + blocking;
  }
}
