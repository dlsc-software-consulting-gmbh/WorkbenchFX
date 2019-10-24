package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.ToolbarControl;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import com.dlsc.workbenchfx.view.controls.module.Tile;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the base for a module, to be displayed in WorkbenchFX.
 *
 * <p>The lifecycle methods <b>must not</b> be called by the implementor, they are being called
 * implicitly by the {@link Workbench}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public abstract class WorkbenchModule {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(WorkbenchModule.class.getName());

  private Workbench workbench;
  private final String name;
  private FontAwesomeIcon faIcon;
  private MaterialDesignIcon mdIcon;
  private Image imgIcon;
  private BooleanProperty closeable = new SimpleBooleanProperty(true);

  // The sets which store the toolbar icons which are displayed in the modules toolbar
  private final ObservableList<ToolbarItem> toolbarControlsLeft =
      FXCollections.observableArrayList();
  private final ObservableList<ToolbarItem> toolbarControlsRight =
      FXCollections.observableArrayList();

  /**
   * Super constructor to be called by the implementing class.
   * Uses a {@link FontAwesomeIcon} as the icon for this module.
   * @see <a href="https://fontawesome.com/v4.7.0/">FontAwesome v4.7.0 Icons</a>
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
   * Uses a {@link MaterialDesignIcon} as the icon for this module.
   * @see <a href="https://materialdesignicons.com/">Material Design Icons</a>
   *
   * @param name of this module
   * @param icon of this module
   */
  protected WorkbenchModule(String name, MaterialDesignIcon icon) {
    this.name = name;
    mdIcon = icon;
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
   * Gets called when there is no {@link Tab} of a module and the module gets opened.
   * This can either be because the module is being opened for the first time, or the module's
   * {@link Tab} has been closed and the module is being re-opened.
   *
   * @param workbench the calling workbench object
   * @implNote Clicking on the {@link Tile} of a module in the add module view will cause the
   *           corresponding module to get opened. When this module is being opened, with
   *           <b>no</b> {@link Tab}s of this module existing yet, the {@link Workbench} will first
   *           call this method. Then, it will create a new {@link Tab} for this module in the
   *           tab bar and open it, which causes {@link #activate()} to get called.<br>
   *           When there is an open {@link Tab} of a module, opening the module will <b>not</b>
   *           cause {@link #init(Workbench)} to be called again. It will only be called if the
   *           module's {@link Tab} was closed and the module is opened again.
   * @implSpec the implementor of this method <b>must</b> call {@code super(Workbench)} to ensure
   *           correct working order.
   */
  public void init(Workbench workbench) {
    this.workbench = workbench;
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
   * @return true if the module should be closed, false if the module should not be closed and the
   *         closing process should be interrupted
   * @implNote <b>Lifecycle:</b> When {@link Workbench#closeModule(WorkbenchModule)} is being called
   *           on an active module, {@link #deactivate()} will be called before {@link #destroy()}
   *           is called. In case of an inactive module, only {@link #destroy()} will be called.
   *           <br>
   *           <b>Return behavior:</b> Assuming Module 1 and Module 2, with both being already
   *           initialized and Module 2 being the currently active and displayed module.
   *           When calling destroy() on Module 1: If true is returned, Module 2 will be removed
   *           and Module 1 will be set as the active module. If false is returned, Module 2 will
   *           not be removed and Module 1 will be set as the new active module, to enable the
   *           user to react to the interrupted closing of the module.
   * @implSpec To implement an asynchronous, controlled closing of a module, execute the immediate
   *           action (e.g. open a dialog) and define the asynchronous behavior in advance to call
   *           {@link #close()} (e.g. define pressing "Yes" on the dialog to call {@link #close()}).
   *           Then <b>return {@code false}</b>, which prevents this module from immediately getting
   *           closed and causes this {@link WorkbenchModule} to get opened, so the user can react.
   *           <br>
   *           Example:
   *           <pre class="code"><code class="java">
   *           getWorkbench().showDialog(WorkbenchDialog.builder("Confirmation", "Close Module?",
   *                                     WorkbenchDialog.Type.CONFIRMATION)
   *                         .blocking(true)
   *                         .onResult(buttonType -&lt; {
   *                           if (ButtonType.YES.equals(buttonType)) {
   *                             // yes was pressed
   *                             close();
   *                           }
   *                         }).build()
   *           );
   *           return false;
   *           </code></pre>
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
   * @implNote Warning! This will <b>definitely</b> close this module!
   *           It will <b>not</b> call {@link #destroy()} before closing it. If you need to clean up
   *           before closing the module, call {@link #destroy()} before calling {@link #close()}.
   */
  public final void close() {
    getWorkbench().completeModuleCloseable(this);
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
   * @return the name of this module.
   */
  public final String getName() {
    return Objects.isNull(name) ? "" : name;
  }

  /**
   * Returns the icon of this module as a {@link Node}.
   * @return the icon of this module as a {@link Node}.
   */
  public final Node getIcon() {
    if (!Objects.isNull(faIcon)) {
      return new FontAwesomeIconView(faIcon);
    } else if (!Objects.isNull(mdIcon)) {
      return new MaterialDesignIconView(mdIcon);
    }
    return new ImageView(imgIcon);
  }

  public boolean isCloseable() {
    return closeable.get();
  }

  public BooleanProperty closeableProperty() {
    return closeable;
  }

  public void setCloseable(boolean closeable) {
    this.closeable.set(closeable);
  }

  /**
   * Returns an {@link ObservableList} which stores the toolbar items of the module.
   * If it's not empty, the {@link Workbench} creates a pre styled {@link ToolbarControl}
   * and adds the stored items on its left side.
   *
   * @return the {@link ObservableList} of items which are displayed on the left side of the
   *         automatically added {@link ToolbarControl}
   */
  public final ObservableList<ToolbarItem> getToolbarControlsLeft() {
    return toolbarControlsLeft;
  }

  /**
   * Returns an {@link ObservableList} which stores the toolbar items of the module.
   * If it's not empty, the {@link Workbench} creates a pre styled {@link ToolbarControl}
   * and adds the stored items on its right side.
   *
   * @return the {@link ObservableList} of items which are displayed on the right side of the
   *         automatically generated {@link ToolbarControl}
   */
  public final ObservableList<ToolbarItem> getToolbarControlsRight() {
    return toolbarControlsRight;
  }
}
