package com.dlsc.workbenchfx.view.module;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.control.SkinBase;

/**
 * Created by François Martin on 15.05.18.
 */
public class PageSkin extends SkinBase<Page> {
  ReadOnlyIntegerProperty pageIndex;

  /**
   * TODO Constructor for all SkinBase instances.
   *
   * @param control The control for which this Skin should attach to.
   */
  public PageSkin(Page control) {
    super(control);
    pageIndex = control.pageIndexProperty();


  }
}
