package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.module.AbstractModule;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class TestModule extends AbstractModule {

  protected TestModule() {
        super("Module Name", new Label("Icon"));
  }

//  protected TestModule() {
//    super("Test Module", GlyphFontRegistry.font("FontAwesome").create("\uf013"));
//  }

  @Override
  public Node init(WorkbenchFx workbench) {
    return new TestView();
  }

}
