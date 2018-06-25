package com.dlsc.workbenchfx.view.dialog;

import com.dlsc.workbenchfx.Workbench;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display dialogs in the {@link Workbench}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class DialogControl extends Control {
  private static final Logger LOGGER =
      LogManager.getLogger(DialogControl.class.getName());

  private ObjectProperty<Workbench> workbench = new SimpleObjectProperty<>();
  private ObjectProperty<WorkbenchDialog> dialog = new SimpleObjectProperty<>();

  /**
   * Creates a dialog control.
   */
  public DialogControl() {
    bindDialog();
  }

  /**
   * Binds {@link Workbench#dialogProperty()} of the workbench to this {@code dialog} property,
   * so the skin doesn't have to account for a potentially changing workbench.
   */
  private void bindDialog() {
    dialog.bind(Bindings.select(workbench, "dialog"));
  }

  public final void hide() {
    getWorkbench().hideDialog();
  }

  public WorkbenchDialog getDialog() {
    return dialog.get();
  }

  public ReadOnlyObjectProperty<WorkbenchDialog> dialogProperty() {
    return dialog;
  }

  private Workbench getWorkbench() {
    return workbench.get();
  }

  public final void setWorkbench(Workbench workbench) {
    this.workbench.set(workbench);
  }

  private ObjectProperty<Workbench> workbenchProperty() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new DialogSkin(this);
  }
}
