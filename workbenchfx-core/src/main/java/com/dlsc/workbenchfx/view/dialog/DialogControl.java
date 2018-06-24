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

  private ObjectProperty<Workbench> workbench = new SimpleObjectProperty<>();

  /**
   * Creates a dialog control.
   */
  public DialogControl() {

  }

  public final void hide() {
    getWorkbench().hideDialog();
  }

  public WorkbenchDialog getDialog() {
    return workbench.get().getDialog();
  }

  public ReadOnlyObjectProperty<WorkbenchDialog> dialogProperty() {
    return workbench.get().dialogProperty();
  }

  private Workbench getWorkbench() {
    return workbench.get();
  }

  public final void setWorkbench(Workbench workbench) {
    this.workbench.set(workbench);
  }

  public ObjectProperty<Workbench> workbenchProperty() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DialogSkin(this);
  }
}
