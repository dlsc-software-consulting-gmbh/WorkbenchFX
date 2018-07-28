package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class ToolbarTestModule extends WorkbenchModule {

  private final Button minusBtn =
      new Button("", new FontAwesomeIconView(FontAwesomeIcon.MINUS));
  private final Button plusBtn =
      new Button("", new FontAwesomeIconView(FontAwesomeIcon.PLUS));
  private final MenuButton userIconBtn =
      new MenuButton("", new FontAwesomeIconView(FontAwesomeIcon.USER),
          new MenuItem("Content")
      );

  private final VBox contentBox = new VBox();

  public ToolbarTestModule() {
    super("Toolbar TestModule", FontAwesomeIcon.QUESTION);

    minusBtn.setOnAction(evt -> {});
    plusBtn.setOnAction(evt -> {});
    userIconBtn.setOnAction(evt -> {});

    getToolbarControlsLeft().add(minusBtn);
    getToolbarControlsLeft().add(plusBtn);
    getToolbarControlsRight().add(userIconBtn);

    contentBox.getStyleClass().add("toolbar-module");
    contentBox.getStylesheets().add(
        ToolbarTestModule.class.getResource("toolbar-module.css").toExternalForm()
    );
  }

  @Override
  public Node activate() {
    return contentBox;
  }
}
