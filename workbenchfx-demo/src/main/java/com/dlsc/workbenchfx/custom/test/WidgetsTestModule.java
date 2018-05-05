package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class WidgetsTestModule extends AbstractModule {
  private final GridPane customPane = new GridPane();

  public WidgetsTestModule() {
    super("Widgets Test", FontAwesomeIcon.QUESTION);
    layoutParts();
  }

  private void layoutParts() {
    customPane.add(new Button("Button"), 0, 0);
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
