package com.dlsc.workbenchfx.demo.modules.helloworld;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class HelloWorldModule extends WorkbenchModule {

  public HelloWorldModule() {
    super("Hello World", MaterialDesignIcon.HUMAN_HANDSUP);
  }

  @Override
  public Node activate() {
    return new HelloWorldView();
  }

}
