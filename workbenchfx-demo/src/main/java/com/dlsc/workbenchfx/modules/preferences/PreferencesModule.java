package com.dlsc.workbenchfx.modules.preferences;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class PreferencesModule extends WorkbenchModule {

  PreferencesView preferencesView;

  public PreferencesModule() {
    super("Preferences", FontAwesomeIcon.GEAR);
    preferencesView = new PreferencesView();
  }

  @Override
  public Node activate() {
    return new PreferencesView();
  }

}
