package com.dlsc.workbenchfx.view.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Created by lemmi on 22.08.17.
 */
public class ToolBarMenuButtonSkin extends SkinBase<ToolBarMenuButton> {

  private final VBox menuContainer = new VBox();

  public ToolBarMenuButtonSkin(ToolBarMenuButton toolBarMenuButton) {
    super(toolBarMenuButton);

    getChildren().addAll();

    toolBarMenuButton.getItems().addListener((Observable it) -> buildMenu());
    buildMenu();
  }

  private void buildMenu() {
    menuContainer.getChildren().clear();
  }
}
