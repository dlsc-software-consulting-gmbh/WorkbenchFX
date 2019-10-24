package com.dlsc.workbenchfx.modules.uncloseable;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class UncloseableModule extends WorkbenchModule {

  public UncloseableModule() {
    super("Uncloseable Module", MaterialDesignIcon.MAGNET);
    this.setCloseable(false);
  }

  @Override
  public Node activate() {
    return new UncloseableView();
  }

}
