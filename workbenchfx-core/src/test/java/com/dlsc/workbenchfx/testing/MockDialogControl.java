package com.dlsc.workbenchfx.testing;

import com.dlsc.workbenchfx.view.controls.dialog.DialogControl;
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
