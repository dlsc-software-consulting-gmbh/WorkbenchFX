package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.Workbench;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;

/**
 * Represents the standard control to be used for the navigation drawer in WorkbenchFX. Is shown in
 * a modal way when the menu button has been pressed, with a {@link GlassPane} in the background.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 * @see <a href="https://material.io/design/components/navigation-drawer.html">Navigation
 * Drawer</a>
 */
public class NavigationDrawer extends Control {

  private ObjectProperty<Workbench> workbench = new SimpleObjectProperty<>();

  /**
   * Creates a navigation drawer control.
   */
  public NavigationDrawer() {

  }

  public final void hide() {
    getWorkbench().hideNavigationDrawer();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new NavigationDrawerSkin(this);
  }

  public final ObservableList<MenuItem> getItems() {
    return getWorkbench().getNavigationDrawerItems();
  }

  public double getWorkbenchWidth() {
    return workbench.get().getWidth();
  }

  public ReadOnlyDoubleProperty workbenchWidthProperty() {
    return workbench.get().widthProperty();
  }

  private Workbench getWorkbench() {
    return workbench.get();
  }

  public final void setWorkbench(Workbench workbench) {
    this.workbench.set(workbench);
  }

  public ObjectProperty<Workbench> workbenchProperty() {
    return workbench;
  }
}
