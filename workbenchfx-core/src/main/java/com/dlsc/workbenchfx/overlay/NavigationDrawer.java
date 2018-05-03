package com.dlsc.workbenchfx.overlay;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
public class NavigationDrawer extends Control implements Overlay {

  private ObservableList<MenuItem> items;
  private WorkbenchFx workbench;

  /**
   * Creates a navigation drawer control.
   *
   * @param workbench to get the items for the menu from
   */
  public NavigationDrawer(WorkbenchFx workbench) {
    getStyleClass().add("navigation-drawer");
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new NavigationDrawerSkin(this);
  }

  public WorkbenchFx getWorkbench() {
    return workbench;
  }

  public final ObservableList<MenuItem> getItems() {
    return items;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node init(WorkbenchFx workbench) {
    this.workbench = workbench;
    items = workbench.getNavigationDrawerItems();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBlocking() {
    return false;
  }
}
