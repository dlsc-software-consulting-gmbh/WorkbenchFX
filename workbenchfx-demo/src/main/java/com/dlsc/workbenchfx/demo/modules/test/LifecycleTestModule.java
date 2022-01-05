package com.dlsc.workbenchfx.demo.modules.test;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class LifecycleTestModule extends WorkbenchModule {

  public LifecycleTestModule() {
    super("Lifecycle Test", MaterialDesign.MDI_HELP);
  }

  @Override
  public void init(Workbench workbench) {
    super.init(workbench);
    System.err.println("Module Lifecycle Phase: init()");
  }

  @Override
  public Node activate() {
    System.err.println("Module Lifecycle Phase: activate()");
    return new Label("This module prints the lifecycle stages to the console");
  }

  @Override
  public void deactivate() {
    System.err.println("Module Lifecycle Phase: deactivate()");
  }

  @Override
  public boolean destroy() {
    System.err.println("Module Lifecycle Phase: destroy()");
    return true;
  }
}
