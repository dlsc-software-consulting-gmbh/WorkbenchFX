package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.NavigationDrawerSkin;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Represents the standard control used to display {@link Module}s as tabs in the toolbar.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tab extends Control {
  private final Workbench workbench;
  private final ObjectProperty<Module> module;

  private final HBox controlBox;
  private final Button closeBtn;

  private final Node icon;
  private final Label nameLbl;
  private final FontAwesomeIconView closeIconView;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Tab(Workbench workbench) {
    this.workbench = workbench;
    module = new SimpleObjectProperty<>();

    this.icon = module.getIcon();
    this.nameLbl = new Label(module.getName());

    closeIconView = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
    this.closeBtn = new Button("", closeIconView);

    controlBox = new HBox();

    layoutParts();
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

  /**
   * Defines the {@code module} which is being represented by this {@link Tab}.
   *
   * @param module to be represented by this {@link Tab}
   */
  public void update(Module module) {
    this.module.set(module);
  }

  public Module getModule() {
    return module.get();
  }

  public ReadOnlyObjectProperty<Module> moduleProperty() {
    return module;
  }

  public Workbench getWorkbench() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new TabSkin(this);
  }

}
