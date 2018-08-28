package com.dlsc.workbenchfx.controls;

import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
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
