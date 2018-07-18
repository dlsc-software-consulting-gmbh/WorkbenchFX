package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.Workbench;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the base for a module, to be displayed in WorkbenchFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public abstract class WorkbenchModule {

  private Workbench workbench;
  private String name;
  private FontAwesomeIcon faIcon;
  private Image imgIcon;
  private CompletableFuture<Boolean> moduleCloseable = new CompletableFuture<>();

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected WorkbenchModule(String name, FontAwesomeIcon icon) {
    this.name = name;
    faIcon = icon;
  }

  /**
   * Super constructor to be called by the implementing class.
   *
   * @param name of this module
   * @param icon of this module
   */
  protected WorkbenchModule(String name, Image icon) {
    this.name = name;
    this.imgIcon = icon;
  }

  // Lifecycle
  /**
   * Gets called when the module is being opened from the overview for the first time.
   *
   * @param workbench the calling workbench object
   * @implSpec the implementor of this method <b>must</b> call {@code super(Workbench)} to ensure
   *           correct working order.
   */
  public void init(Workbench workbench) {
    this.workbench = workbench;
    moduleCloseable = new CompletableFuture<>(); // reset in case module was destroyed before
  }

  /**
   * Gets called whenever the currently displayed content is being switched to this module.
   *
   * @return content to be displayed in this module
   * @implNote if a module is being opened from the overview for the first time, it will get
   *           initialized first by calling init(), afterwards activate() will be called.
   */
  public abstract Node activate();

  /**
   * Gets called whenever this module is the currently displayed content and the content is being
   * switched to another module.
   *
   * @implNote Assuming Module 1 and Module 2, with both being already initialized and Module 1
   *           being the currently displayed content.
   *           When switching the content to Module 2, deactivate() gets called on Module 1,
   *           followed by a call of activate() on Module 2.
   */
  public void deactivate() {
  }

  /**
   * Gets called when this module is explicitly being closed by the user in the toolbar.
   *
   * @return true if successful
   * @implNote Assuming Module 1 and Module 2, with both being already initialized and Module 2
   *           being the currently active and displayed module.
   *           When calling destroy() on Module 1: If true is returned, Module 2 will be removed
   *           and Module 1 will be set as the active module. If false is returned, Module 2 will
   *           not be removed and Module 1 will be set as the new active module, to enable the
   *           user to react to the interrupted closing of the module.
   */
  public boolean destroy() {
    return true;
  }

  public final Workbench getWorkbench() {
    return workbench;
  }

  /**
   * Closes this module.
   *
   * @implNote Warning! This will <b>definitely</b> close this module! Any unsaved changes are lost.
   *           If you need to clean up before closing the module, call {@link #destroy()} before
   *           calling {@link #close()}.
   */
  public final void close() {
    getModuleCloseable().complete(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Returns the name of this module.
   */
  public String getName() {
    return Objects.isNull(name) ? "" : name;
  }

  /**
   * Returns the icon of this module as a {@link Node}.
   */
  public Node getIcon() {
    return Objects.isNull(faIcon) ? new ImageView(imgIcon) : new FontAwesomeIconView(faIcon);
  }

  /**
   * TODO
   * @return
   */
  public CompletableFuture<Boolean> getModuleCloseable() {
    return moduleCloseable;
  }
}
