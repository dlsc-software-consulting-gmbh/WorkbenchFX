package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.Workbench;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.scene.layout.Priority;

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

  private final ObjectProperty<Workbench> workbench = new SimpleObjectProperty<>(this, "workbench");

  private final ObjectProperty<Priority> menuHoverBehavior = new SimpleObjectProperty<>(
      this, "menuHoverBehavior", Priority.ALWAYS);

  /**
   * Creates a navigation drawer control.
   */
  public NavigationDrawer() {
    getStyleClass().add("navigation-drawer");
  }

  public final void hide() {
    getWorkbench().hideNavigationDrawer();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new NavigationDrawerSkin(this);
  }


  public final Priority getMenuHoverBehavior() {
    return menuHoverBehavior.get();
  }

  /**
   * Defines the showing behaviour of the {@link MenuItem}s when hovering over them.
   * The default value is set to ALWAYS.
   * ALWAYS:    Triggers whenever the mouse hovers over a {@link MenuItem}
   * SOMETIMES: Requires one initial click on a {@link MenuItem} to open.
   *            After that, hovering over other Items shows their submenus automatically
   * NEVER:     No hover behaviour on the {@link MenuItem}s
   * @defaultValue Priority.ALWAYS
   * @return the property of the menu hover behavior
   */
  public final ObjectProperty<Priority> menuHoverBehaviorProperty() {
    return menuHoverBehavior;
  }

  public final void setMenuHoverBehavior(Priority menuHoverBehavior) {
    this.menuHoverBehavior.set(menuHoverBehavior);
  }

  public final ObservableList<MenuItem> getItems() {
    return getWorkbench().getNavigationDrawerItems();
  }

  public final Workbench getWorkbench() {
    return workbench.get();
  }

  public final void setWorkbench(Workbench workbench) {
    this.workbench.set(workbench);
  }

  public final ObjectProperty<Workbench> workbenchProperty() {
    return workbench;
  }
}
