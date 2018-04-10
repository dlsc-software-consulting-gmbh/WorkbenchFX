package com.dlsc.workbenchfx.standard.preferences;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class PreferencesModule extends AbstractModule {

  public PreferencesModule() {
    super("Preferences", FontAwesomeIcon.GEAR);
  }

  @Override
  public Node activate() {
    return new PreferencesView();
  }

}
