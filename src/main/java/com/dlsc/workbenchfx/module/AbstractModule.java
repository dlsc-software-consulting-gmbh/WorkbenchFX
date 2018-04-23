package com.dlsc.workbenchfx.module;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.util.WorkbenchFxUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Skeletal implementation of a {@link Module}. Extend this class to simply implement a new module
 * and override methods as needed.
 */
public abstract class AbstractModule implements Module {

  protected WorkbenchFx workbench;
  private String name;
  private FontAwesomeIcon faIcon;
  private Image imgIcon;

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, Image icon) {
    this.name = name;
    this.imgIcon = icon;
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected AbstractModule(String name, FontAwesomeIcon icon) {
    this.name = name;
    faIcon = icon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(WorkbenchFx workbench) {
    this.workbench = workbench;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node getGraphic() {
    return Objects.isNull(faIcon) ? new ImageView(imgIcon) : new FontAwesomeIconView(faIcon);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return Objects.isNull(name) ? "" : name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deactivate() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean destroy() {
    return true;
  }
}
