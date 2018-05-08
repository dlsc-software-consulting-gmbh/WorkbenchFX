package com.dlsc.workbenchfx.extended.preferences;

import com.dlsc.workbenchfx.module.Module;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class PreferencesModule extends Module {

  public PreferencesModule() {
    super("Preferences", FontAwesomeIcon.GEAR);
  }

  @Override
  public Node activate() {
    return new PreferencesView();
  }

}
