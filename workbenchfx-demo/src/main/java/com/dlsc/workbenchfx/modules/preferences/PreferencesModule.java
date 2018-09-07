package com.dlsc.workbenchfx.modules.preferences;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;

public class PreferencesModule extends WorkbenchModule {

  Preferences preferences;

  public PreferencesModule(Preferences preferences) {
    super("Preferences", FontAwesomeIcon.GEAR);
    this.preferences = preferences;

    ToolbarItem save = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE),
        event -> preferences.save());
    ToolbarItem discardChanges =
        new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.DELETE),
            event -> getWorkbench().showConfirmationDialog("Discard Changes",
                "Are you sure you want to discard all changes since you last saved?",
                buttonType -> {
                  if (ButtonType.YES.equals(buttonType)) {
                    preferences.discardChanges();
                  }
                })
        );
    getToolbarControlsLeft().addAll(save, discardChanges);
  }

  @Override
  public Node activate() {
    return preferences.getPreferencesFxView();
  }

  @Override
  public boolean destroy() {
    preferences.save();
    return true;
  }
}
