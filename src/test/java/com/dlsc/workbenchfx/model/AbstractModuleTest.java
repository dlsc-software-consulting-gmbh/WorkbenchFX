package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.model.module.AbstractModule;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.junit.jupiter.api.BeforeEach;

public class AbstractModuleTest {
  // Test implementation
  public static class TestView extends BorderPane {
    public TestView() {
      setCenter(new Label("If you can read this, the test view has been successfully shown!"));
    }
  }

  public static class TestModule extends AbstractModule {
    protected TestModule() {
      super("Test Module", GlyphFontRegistry.font("FontAwesome").create("\uf013"), new TestView());
    }
  }

  @BeforeEach
  void setUp() {

  }

}
