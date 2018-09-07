package com.dlsc.workbenchfx.modules.preferences;

import com.dlsc.preferencesfx.view.PreferencesFxView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class PreferencesModule extends WorkbenchModule {

  PreferencesFxView preferencesFxView;

  public PreferencesModule(Preferences preferences) {
    super("Preferences", FontAwesomeIcon.GEAR);
    this.preferencesFxView = preferences.getPreferencesFxView();
    //ToolbarItem save = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE), )
    //getToolbarControlsLeft().add()
  }

  @Override
  public Node activate() {
    return preferencesFxView;
  }

}
