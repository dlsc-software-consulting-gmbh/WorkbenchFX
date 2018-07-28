package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToolbarTestModule extends WorkbenchModule {

  private static final Logger LOGGER =
      LogManager.getLogger(ToolbarTestModule.class.getName());

  private final Button minusBtn =
      new Button("", new FontAwesomeIconView(FontAwesomeIcon.MINUS));
  private final Button plusBtn =
      new Button("", new FontAwesomeIconView(FontAwesomeIcon.PLUS));
  private final MenuItem addContentItem = new MenuItem("Add Content");
  private final MenuItem removeContentItem = new MenuItem("Remove Content");
  private final Dropdown addContentDropdown = Dropdown.of(
      "ADD CONTENT", new FontAwesomeIconView(FontAwesomeIcon.USER),
      addContentItem, removeContentItem
  );
  private int contentIndex = 1;

  private final VBox contentBox = new VBox();

  public ToolbarTestModule() {
    super("Toolbar TestModule", FontAwesomeIcon.QUESTION);

    minusBtn.setOnAction(evt -> {
      LOGGER.trace("getToolbarControlsRight().size() = " + getToolbarControlsRight().size());
      getToolbarControlsRight().remove(addContentDropdown);
      LOGGER.trace("getToolbarControlsRight().size() = " + getToolbarControlsRight().size());
    });
    plusBtn.setOnAction(evt -> {
      LOGGER.trace("getToolbarControlsRight().size() = " + getToolbarControlsRight().size());
      getToolbarControlsRight().add(addContentDropdown);
      LOGGER.trace("getToolbarControlsRight().size() = " + getToolbarControlsRight().size());
    });
    addContentItem.setOnAction(evt ->
        contentBox.getChildren().add(new Label("Content " + contentIndex++))
    );
    removeContentItem.setOnAction(evt -> {
      if (--contentIndex < 1) {
        contentIndex = 1;
      } else {
        contentBox.getChildren().remove(contentIndex-1);
      }
    });

    getToolbarControlsLeft().add(minusBtn);
    getToolbarControlsLeft().add(plusBtn);
    getToolbarControlsRight().add(addContentDropdown);

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
