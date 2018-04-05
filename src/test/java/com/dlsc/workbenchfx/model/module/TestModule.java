package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.Node;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class TestModule extends AbstractModule {

  protected TestModule() {
    super("Test Module", GlyphFontRegistry.font("FontAwesome").create("\uf013"));
  }

  @Override
  public Node init(WorkbenchFx workbench) {
    return new TestView();
  }

}
