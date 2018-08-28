package com.dlsc.workbenchfx.modules.maps;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class MapsModule extends WorkbenchModule {

  public MapsModule() {
    super("Maps", FontAwesomeIcon.MAP_SIGNS);
  }

  @Override
  public Node activate() {
    try {
      return new MapsView();
    } catch (Exception e) {
      return new Label("Couldn't establish a connection to Maps." +
          "\nMake sure you are connected to the internet and try again.");
    }
  }

}
