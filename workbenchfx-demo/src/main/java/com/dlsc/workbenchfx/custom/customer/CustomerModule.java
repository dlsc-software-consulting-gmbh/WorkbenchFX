package com.dlsc.workbenchfx.custom.customer;

import com.dlsc.workbenchfx.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class CustomerModule extends AbstractModule {

  public CustomerModule() {
    super("Customer Management", FontAwesomeIcon.USERS);
  }

  @Override
  public Node activate() {
    return new CustomerView();
  }

}
