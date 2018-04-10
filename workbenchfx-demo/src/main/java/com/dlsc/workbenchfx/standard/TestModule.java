package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class TestModule extends AbstractModule {

  protected TestModule() {
    super("Test Module", FontAwesomeIcon.ANGELLIST);
  }

  @Override
  public Node init(WorkbenchFx workbench) {
    return new TestView();
  }

}
