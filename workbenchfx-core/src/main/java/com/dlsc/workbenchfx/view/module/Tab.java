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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link Module}s as tabs in the toolbar.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tab extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(Tab.class.getName());

  private final Workbench workbench;
  private final ObjectProperty<Module> module;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Tab(Workbench workbench) {
    this.workbench = workbench;
    module = new SimpleObjectProperty<>();
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
