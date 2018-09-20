package com.dlsc.workbenchfx.demo.modules.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToolbarTestModule extends WorkbenchModule {

  private static final Logger LOGGER =
      LogManager.getLogger(ToolbarTestModule.class.getName());

  private final Button removeItemsBtn = new Button("Remove all items from the toolbar");
  private final Button addItemsBtn = new Button("Add all items to the toolbar");
  private final MenuItem addContentItem = new MenuItem("Add Content");
  private final MenuItem removeContentItem = new MenuItem("Remove Content");

  private final ToolbarItem addContentToolbarItem = new ToolbarItem("Add Content",
      new FontAwesomeIconView(FontAwesomeIcon.USER), addContentItem, removeContentItem);
  private final ToolbarItem remBtn = new ToolbarItem(new FontAwesomeIconView(FontAwesomeIcon.MINUS),
      event -> getToolbarControlsRight().remove(addContentToolbarItem));
  private final ToolbarItem addBtn = new ToolbarItem(new FontAwesomeIconView(FontAwesomeIcon.PLUS),
      event -> {
        if (!getToolbarControlsRight().contains(addContentToolbarItem)) {
          getToolbarControlsRight().add(addContentToolbarItem);
        }
      });

  private int contentIndex = 1;

  private final VBox contentBox = new VBox();
  private final VBox topBox = new VBox();
  private final HBox bottomBox = new HBox();

  public ToolbarTestModule() {
    super("Toolbar Test Module", MaterialDesignIcon.HELP);
    addToolbarItems();
    setupStyling();
    layoutParts();
    setupEventHandlers();
  }

  private void addToolbarItems() {
    if (getToolbarControlsLeft().isEmpty()) {
      getToolbarControlsLeft().addAll(remBtn, addBtn, new ToolbarItem("Module Toolbar"));
      getToolbarControlsRight().add(addContentToolbarItem);
    }
  }

  private void setupStyling() {
    contentBox.getStyleClass().add("toolbar-module");
    contentBox.getStylesheets().add(
        ToolbarTestModule.class.getResource("toolbar-module.css").toExternalForm()
    );
    contentBox.setStyle("-fx-background-color: #f2f2f2;");

    contentBox.setAlignment(Pos.CENTER);
    topBox.setAlignment(Pos.CENTER);
    VBox.setVgrow(topBox, Priority.ALWAYS);
    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.setMinHeight(100);
    bottomBox.setSpacing(25);
  }

  private void layoutParts() {
    topBox.getChildren().addAll(
        new Label("Module to test the modules toolbar."),
        new Label("Use the toolbar item to add content."),
        addItemsBtn, removeItemsBtn
    );

    contentBox.getChildren().addAll(topBox, bottomBox);
  }

  private void setupEventHandlers() {
    addContentItem.setOnAction(
        event -> bottomBox.getChildren().add(new Label("Content " + contentIndex++))
    );

    removeContentItem.setOnAction(event -> {
      if (--contentIndex < 1) {
        contentIndex = 1;
      } else {
        bottomBox.getChildren().remove(contentIndex - 1);
      }
    });

    addItemsBtn.setOnAction(event -> addToolbarItems());
    removeItemsBtn.setOnAction(event -> removeToolbarItems());
  }

  @Override
  public Node activate() {
    return contentBox;
  }

  private void removeToolbarItems() {
    getToolbarControlsLeft().clear();
    getToolbarControlsRight().clear();
  }
}
