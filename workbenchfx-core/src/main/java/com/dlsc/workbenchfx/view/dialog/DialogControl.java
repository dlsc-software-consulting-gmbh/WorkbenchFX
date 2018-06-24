package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import com.dlsc.workbenchfx.view.controls.DropdownSkin;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogControl extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogControl.class.getName());

  private final ReadOnlyObjectProperty<WorkbenchDialog> dialog;

  /**
   * Creates a dialog control.
   *
   * @param workbench which created this {@link DialogControl}
   */
  public DialogControl(Workbench workbench) {
    dialog = workbench.dialogProperty();
  }

  public WorkbenchDialog getDialog() {
    return dialog.get();
  }

  public ReadOnlyObjectProperty<WorkbenchDialog> dialogProperty() {
    return dialog;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DialogSkin(this);
  }
}
