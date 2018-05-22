package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the standard control used to display {@link Page}s with {@link Module}s in the home
 * screen.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Page extends Control {
  private static final Logger LOGGER = LogManager.getLogger(Page.class.getName());
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
   * Defines the {@code pageIndex} of this {@link Page}.
   *
   * @param pageIndex to be represented by this {@link Page}
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

  public Workbench getWorkbench() {
    return workbench;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new PageSkin(this);
  }
}
