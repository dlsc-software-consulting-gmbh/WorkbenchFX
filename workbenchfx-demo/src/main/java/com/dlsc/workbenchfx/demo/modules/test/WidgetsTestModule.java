package com.dlsc.workbenchfx.demo.modules.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class WidgetsTestModule extends WorkbenchModule {
  private final GridPane customPane = new GridPane();

  public WidgetsTestModule() {
    super("Widgets Test", MaterialDesignIcon.HELP);
    layoutParts();
  }

  private void layoutParts() {
    customPane.add(new Label("Label"), 0, 1);
    customPane.add(new TextField("TextField"), 0, 2);
    customPane.add(new CheckBox("CheckBox"), 0, 3);
    customPane.add(new ComboBox<String>(), 0, 4);
    customPane.setAlignment(Pos.CENTER);
  }

  @Override
  public Node activate() {
    return customPane;
  }
}
