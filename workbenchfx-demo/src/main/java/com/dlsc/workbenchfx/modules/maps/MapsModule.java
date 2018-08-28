package com.dlsc.workbenchfx.modules.maps;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class MapsModule extends WorkbenchModule {

  public MapsModule() {
    super("Maps", FontAwesomeIcon.MAP_SIGNS);
  }

  @Override
  public Node activate() {
    return new MapsView();
  }

}
