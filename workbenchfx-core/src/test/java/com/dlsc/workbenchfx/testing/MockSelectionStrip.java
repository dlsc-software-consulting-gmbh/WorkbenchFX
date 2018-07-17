package com.dlsc.workbenchfx.testing;

import com.dlsc.workbenchfx.view.controls.selectionstrip.SelectionStrip;
import javafx.scene.control.Skin;

public class MockSelectionStrip extends SelectionStrip {

  public MockSelectionStrip() {
    super();
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new MockSelectionStripSkin(this);
  }
}
