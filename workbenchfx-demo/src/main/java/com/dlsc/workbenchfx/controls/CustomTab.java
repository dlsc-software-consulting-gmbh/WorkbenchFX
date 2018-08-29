package com.dlsc.workbenchfx.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.scene.control.Skin;

public class CustomTab extends Tab {
  /**
   * Constructs a new {@link CustomTab}.
   *
   * @param workbench which created this {@link CustomTab}
   */
  public CustomTab(Workbench workbench) {
    super(workbench);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new CustomTabSkin(this);
  }
}
