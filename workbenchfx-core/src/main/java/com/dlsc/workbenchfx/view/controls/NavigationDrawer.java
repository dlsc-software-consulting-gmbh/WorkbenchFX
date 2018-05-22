package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.Workbench;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;

/**
 * Represents the standard control to be used for the navigation drawer in WorkbenchFX. Is shown in
 * a modal way when the menu button has been pressed, with a {@link GlassPane} in the background.
 *
 * @see <a href="https://material.io/guidelines/patterns/navigation-drawer.html">Navigation
 *     Drawer</a>
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public class NavigationDrawer extends Control {

  private ObservableList<MenuItem> items;
  private Workbench workbench;
  private final DoubleProperty drawerWidth = new SimpleDoubleProperty();

  /**
   * Creates a navigation drawer control.
   */
  public NavigationDrawer(Workbench workbench) {
    this.workbench = workbench;
    items = workbench.getNavigationDrawerItems();
    drawerWidth.bind(workbench.widthProperty().multiply(.333));
    getStyleClass().add("navigation-drawer");
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new NavigationDrawerSkin(this);
  }

  public Workbench getWorkbench() {
    return workbench;
  }

  public final ObservableList<MenuItem> getItems() {
    return items;
  }

  public double getDrawerWidth() {
    return drawerWidth.get();
  }

  public DoubleProperty drawerWidthProperty() {
    return drawerWidth;
  }
}
