package com.dlsc.workbenchfx.custom.overlay;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.test.InterruptClosingTestModule;
import com.dlsc.workbenchfx.module.Module;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * TODO: Refactor once dialogs are merged.
 * TODO: Maybe include this dialog as an easier option to use?
 */
public class CustomDialog extends BorderPane {

  private final InterruptClosingTestModule module;
  private Workbench workbench;
  private HBox buttonBox;

  public CustomDialog(Workbench workbench, InterruptClosingTestModule module) {
    this.module = module;
    Objects.requireNonNull(workbench);
    this.workbench = workbench;
    init();
  }

  private void init() {
    getStyleClass().add("custom-overlay");

    Label centerLbl = new Label("Are you sure you want to close this module without saving?");
    centerLbl.getStyleClass().add("centerLbl");
    setCenter(centerLbl);

    Button yesBtn = new Button("Yes");
    Button noBtn = new Button("No");
    yesBtn.setOnAction(event -> {
      module.setClosePossible(true);
      workbench.closeModule(module);
      workbench.hideOverlay(this);
    });
    noBtn.setOnAction(event -> {
      workbench.hideOverlay(this);
    });
    buttonBox = new HBox(yesBtn, noBtn);
    BorderPane.setAlignment(buttonBox, Pos.CENTER);
    setBottom(buttonBox);
  }
}
