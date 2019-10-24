package com.dlsc.workbenchfx.view.controls.module;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.util.WorkbenchUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the standard control used to display {@link WorkbenchModule}s as tabs in the toolbar.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Tab extends Control {

  private static final Logger LOGGER = LoggerFactory.getLogger(Tab.class.getName());

  private final Workbench workbench;
  private final ObjectProperty<WorkbenchModule> module;
  private final StringProperty name;
  private final ObjectProperty<Node> icon;
  private final BooleanProperty activeTab;
  private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
  private final BooleanProperty closeable;

  /**
   * Constructs a new {@link Tab}.
   *
   * @param workbench which created this {@link Tab}
   */
  public Tab(Workbench workbench) {
    this.workbench = workbench;
    module = new SimpleObjectProperty<>(this, "module");
    name = new SimpleStringProperty(this, "name");
    icon = new SimpleObjectProperty<>(this, "icon");
    activeTab = new SimpleBooleanProperty(this, "activeTab");
    closeable = new SimpleBooleanProperty(this, "closeable");
    setupModuleListeners();
    setupActiveTabListener();
    setupEventHandlers();
    getStyleClass().add("tab-control");
  }

  private void setupEventHandlers() {
    setOnMouseClicked(e -> open());
  }

  private void setupModuleListeners() {
    module.addListener(observable -> {
      WorkbenchModule current = getModule();
      // Replace any occurence of \n with space
      name.setValue(current.getName().replace("\n", " "));
      icon.setValue(current.getIcon());
      closeable.unbind();
      closeable.bind(current.closeableProperty());

      // Sets the id with toString of module.
      // Adds 'tab-', replaces spaces with hyphens and sets letters to lowercase.
      // eg. Customer Management converts to tab-customer-management
      String tabId = WorkbenchUtils.convertToId("tab-" + current.getName());
      LOGGER.debug("Set Tab-ID of '" + getModule() + "' to: '" + tabId + "'");
      setId(tabId);
    });
  }

  private void setupActiveTabListener() {
    // whenever the module of this tab changes, re-initialize the binding which determines whether
    // this tab is the currently active tab or not
    moduleProperty().addListener(observable -> {
      activeTab.unbind();
      activeTab.bind(Bindings.equal(getModule(), workbench.activeModuleProperty()));
    });
    activeTab.addListener((observable, oldValue, newValue) ->
        pseudoClassStateChanged(SELECTED, newValue)
    );
  }

  /**
   * Closes the {@link WorkbenchModule} along with this {@link Tab}.
   */
  public final void close() {
    workbench.closeModule(getModule());
  }

  /**
   * Opens the {@link WorkbenchModule} belonging to this {@link Tab}.
   */
  public final void open() {
    workbench.openModule(getModule());
  }

  public final WorkbenchModule getModule() {
    return module.get();
  }

  /**
   * Defines the {@code module} which is being represented by this {@link Tab}.
   *
   * @param module to be represented by this {@link Tab}
   */
  public final void setModule(WorkbenchModule module) {
    LOGGER.trace("Setting reference to module");
    this.module.set(module);
  }

  public final ReadOnlyObjectProperty<WorkbenchModule> moduleProperty() {
    return module;
  }

  public final String getName() {
    return name.get();
  }

  public final ReadOnlyStringProperty nameProperty() {
    return name;
  }

  public final Node getIcon() {
    return icon.get();
  }

  public final ReadOnlyObjectProperty<Node> iconProperty() {
    return icon;
  }

  public final boolean isActiveTab() {
    return activeTab.get();
  }

  public final ReadOnlyBooleanProperty activeTabProperty() {
    return activeTab;
  }

  public boolean isCloseable() {
    return closeable.get();
  }

  public ReadOnlyBooleanProperty closeableProperty() {
    return closeable;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new TabSkin(this);
  }
}
