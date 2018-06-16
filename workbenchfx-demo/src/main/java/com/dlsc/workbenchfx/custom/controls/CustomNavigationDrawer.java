package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;

public class CustomNavigationDrawer extends NavigationDrawer {

  /**
   * Creates a navigation drawer control.
   */
  public CustomNavigationDrawer() {
    super();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new CustomNavigationDrawerSkin(this);
  }

}
