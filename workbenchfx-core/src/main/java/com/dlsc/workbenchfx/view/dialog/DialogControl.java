package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import com.dlsc.workbenchfx.view.controls.DropdownSkin;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogControl extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogControl.class.getName());

  /**
   * Creates a dialog control.
   *
   * @param workbench which created this {@link DialogControl}
   */
  public DialogControl(Workbench workbench) {

  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DialogSkin(this);
  }
}
