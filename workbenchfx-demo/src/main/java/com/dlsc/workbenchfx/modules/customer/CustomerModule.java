package com.dlsc.workbenchfx.modules.customer;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class CustomerModule extends WorkbenchModule {

  public CustomerModule() {
    super("Customer\nManagement\nModule", MaterialDesignIcon.ACCOUNT_MULTIPLE);
  }

  @Override
  public Node activate() {
    return new CustomerView();
  }

}
