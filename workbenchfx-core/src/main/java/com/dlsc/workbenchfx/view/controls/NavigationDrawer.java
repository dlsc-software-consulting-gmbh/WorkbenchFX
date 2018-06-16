package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.Workbench;
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

  /**
   * Creates a navigation drawer control.
   */
  public NavigationDrawer(Workbench workbench) {
    items = workbench.getNavigationDrawerItems();
  }

  public void hide() {
    workbench.hideOverlay(this, false);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new NavigationDrawerSkin(this);
  }

  public final ObservableList<MenuItem> getItems() {
    return items;
  }
}
