package com.dlsc.workbenchfx.demo.modules.helloworld;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class HelloWorldModule extends WorkbenchModule {

  public HelloWorldModule() {
    super("Hello World", MaterialDesign.MDI_HUMAN_HANDSUP);
  }

  @Override
  public Node activate() {
    return new HelloWorldView();
  }

}
