package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class ToolbarTestModule extends WorkbenchModule {

  public ToolbarTestModule() {
    super("Toolbar TestModule", FontAwesomeIcon.QUESTION);
  }

  @Override
  public Node activate() {
    return null;
  }
}
