package com.dlsc.workbenchfx.custom.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.module.Page;
import com.dlsc.workbenchfx.view.module.Tab;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomPage extends Page {
  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public CustomPage(Workbench workbench) {
    super(workbench);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new CustomPageSkin(this);
  }
}
