package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.model.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class TestModule extends AbstractModule {

  protected TestModule() {
//    super("Test Module", GlyphFontRegistry.font("FontAwesome").create("\uf013"));
    super("Test Module", new FontAwesomeIconView(FontAwesomeIcon.ANGELLIST));
  }

  @Override
  public Node init(WorkbenchFx workbench) {
    return new TestView();
  }

}
