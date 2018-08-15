package com.dlsc.workbenchfx.custom.preferences;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class PreferencesModule extends WorkbenchModule {

  public PreferencesModule() {
    super("Preferences", MaterialDesignIcon.SETTINGS);
  }

  @Override
  public Node activate() {
    return new PreferencesView();
  }

}
