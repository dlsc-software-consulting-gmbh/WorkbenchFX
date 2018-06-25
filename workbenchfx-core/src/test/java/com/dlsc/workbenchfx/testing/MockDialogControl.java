package com.dlsc.workbenchfx.testing;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Page;
import com.dlsc.workbenchfx.view.dialog.DialogControl;
import javafx.scene.control.Skin;

public class MockDialogControl extends DialogControl {

  public MockDialogControl() {
    super();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new MockDialogSkin(this);
  }
}
