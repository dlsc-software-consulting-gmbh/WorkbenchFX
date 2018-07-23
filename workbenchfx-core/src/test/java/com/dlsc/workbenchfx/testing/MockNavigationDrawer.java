package com.dlsc.workbenchfx.testing;

import com.dlsc.workbenchfx.view.controls.NavigationDrawer;
import javafx.scene.control.Skin;

public class MockNavigationDrawer extends NavigationDrawer {

  public MockNavigationDrawer() {
    super();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new MockNavigationDrawerSkin(this);
  }
}
