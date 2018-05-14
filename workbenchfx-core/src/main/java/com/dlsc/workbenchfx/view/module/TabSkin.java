package com.dlsc.workbenchfx.view.module;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_TAB;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * TODO
 */
public class TabSkin extends SkinBase<Tab> {

  private final HBox controlBox;
  private final Button closeBtn;

  private final Node icon;
  private final Label nameLbl;
  private final FontAwesomeIconView closeIconView;

  /**
   * Creates a new {@link TabSkin} object for a corresponding {@link Tab}.
   *
   * @param tab the {@link Tab} for which this Skin is created
   */
  public TabSkin(Tab tab) {
    super(tab);

    this.icon = module.getIcon();
    this.nameLbl = new Label(module.getName());

    closeIconView = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
    this.closeBtn = new Button("", closeIconView);

    controlBox = new HBox();

    layoutParts();

    workbench.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
      LOGGER.trace("Tab Factory - Old Module: " + oldModule);
      LOGGER.trace("Tab Factory - New Module: " + oldModule);
      if (module == newModule) {
        tab.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
        LOGGER.trace("STYLE SET");
      }
      if (module == oldModule) {
        // switch from this to other tab
        tab.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
      }
    });
    tab.setOnClose(e -> workbench.closeModule(module));
    tab.setOnActive(e -> workbench.openModule(module));
    tab.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
    return tab;
  };
  }

  private void layoutParts() {
    controlBox.getChildren().addAll(
        icon,
        nameLbl,
        closeBtn
    );

    icon.getStyleClass().add("tab-icon");
    nameLbl.getStyleClass().add("tab-name-lbl");

    closeBtn.getStyleClass().add("close-btn");
    closeIconView.setStyleClass("close-icon-view");

    controlBox.getStyleClass().add("tab-control");
  }

  /**
   * Defines the {@link EventHandler} which should be called when the close button on this tab is
   * being pressed.
   *
   * @param event to be called
   */
  public void setOnClose(EventHandler<ActionEvent> event) {
    closeBtn.setOnAction(event);
  }

  /**
   * Defines the {@link EventHandler} which should be called when this control is being clicked on,
   * setting the tab active.
   *
   * @param event to be called
   */
  public void setOnActive(EventHandler<MouseEvent> event) {
    controlBox.setOnMouseClicked(event);
  }

}
