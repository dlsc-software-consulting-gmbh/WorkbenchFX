package com.dlsc.workbenchfx.modules.preferences;

import com.dlsc.preferencesfx.view.PreferencesFxView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class PreferencesModule extends WorkbenchModule {

  PreferencesFxView preferencesFxView;

  public PreferencesModule(PreferencesFxView preferencesFxView) {
    super("Preferences", FontAwesomeIcon.GEAR);
    this.preferencesFxView = preferencesFxView;
  }

  @Override
  public Node activate() {
    return preferencesFxView;
  }

}
