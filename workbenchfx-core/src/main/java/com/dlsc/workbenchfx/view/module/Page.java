package com.dlsc.workbenchfx.view.module;

import com.dlsc.workbenchfx.Workbench;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Created by François Martin on 15.05.18.
 */
public class Page extends Control {
  private final Workbench workbench;
  private final IntegerProperty pageIndex;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Page(Workbench workbench) {
    this.workbench = workbench;
    pageIndex = new SimpleIntegerProperty();
  }

  /**
   * TODO Defines the {@code module} which is being represented by this {@link Tab}.
   *
   * @param module to be represented by this {@link Tab}
   */
  public void update(int pageIndex) {
    this.pageIndex.set(pageIndex);
  }

  public int getPageIndex() {
    return pageIndex.get();
  }

  public ReadOnlyIntegerProperty pageIndexProperty() {
    return pageIndex;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new PageSkin(this);
  }
}
