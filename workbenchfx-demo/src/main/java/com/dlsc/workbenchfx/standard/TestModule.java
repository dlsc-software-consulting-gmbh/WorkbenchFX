package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class TestModule extends AbstractModule {

  protected TestModule() {
    super("Test Module", FontAwesomeIcon.ANGELLIST);
  }

  @Override
  public Node init(WorkbenchFxModel workbench) {
    return new TestView();
  }

}
