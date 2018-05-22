package com.dlsc.workbenchfx.custom.controls;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.module.Module;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Objects;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link CustomTab}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CustomTabSkin extends SkinBase<CustomTab> {
  private static final Logger LOGGER = LogManager.getLogger(
      CustomTabSkin.class.getName());

  private final ReadOnlyObjectProperty<Module> module;

  private HBox controlBox;
  private Button closeBtn;
  private FontAwesomeIconView closeIconView;

  private Node icon;
  private Label nameLbl;
  private ChangeListener<Module> activeCustomTabListener;

  /**
   * Creates a new {@link CustomTabSkin} object for a corresponding {@link CustomTab}.
   *
   * @param customTab the {@link CustomTab} for which this Skin is created
   */
  public CustomTabSkin(CustomTab customTab) {
    super(customTab);
    module = customTab.moduleProperty();

    initializeParts();
    layoutParts();

    Workbench workbench = customTab.getWorkbench();
    setupSkin(workbench, module.get()); // initial setup
    setupModuleListener(workbench); // setup for changing modules

    getChildren().add(controlBox);
  }

  private void initializeParts() {
    closeIconView = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
    this.closeBtn = new Button("", closeIconView);

    nameLbl = new Label();
    controlBox = new HBox();
  }

  private void layoutParts() {
    Label iconPlaceholder = new Label();
    controlBox.getChildren().addAll(iconPlaceholder, nameLbl, closeBtn);

    nameLbl.getStyleClass().add("tab-name-lbl");

    closeBtn.getStyleClass().add("close-btn");
    closeIconView.setStyleClass("close-icon-view");

    controlBox.getStyleClass().add("tab-control");
    controlBox.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
  }

  private void setupModuleListener(Workbench workbench) {
    LOGGER.trace("Add module listener");
    module.addListener((observable, oldModule, newModule) -> {
      LOGGER.trace("moduleListener called");
      LOGGER.trace("old: " + oldModule + " new: " + newModule);
      if (oldModule != newModule) {
        LOGGER.trace("Setting up skin");
        setupSkin(workbench, newModule);
      }
    });
  }

  private void setupSkin(Workbench workbench, Module module) {
    setupIcon(module);
    nameLbl.setText(module.getName());
    closeBtn.setOnAction(e -> workbench.closeModule(module));
    controlBox.setOnMouseClicked(e -> workbench.openModule(module));
    setupActiveCustomTabListener(workbench, module);
  }

  private void setupIcon(Module module) {
    // remove old and add new icon
    controlBox.getChildren().remove(0);
    this.icon = module.getIcon();
    controlBox.getChildren().add(0, icon);
    icon.getStyleClass().add("tab-icon");
  }

  private void setupActiveCustomTabListener(Workbench workbench, Module module) {
    // remove previously set listener
    if (!Objects.isNull(activeCustomTabListener)) {
      workbench.activeModuleProperty().removeListener(activeCustomTabListener);
    }

    // (re-)initialize active customTab listener
    activeCustomTabListener = (observable, oldModule, newModule) -> {
      LOGGER.trace("CustomTab Factory - Old Module: " + oldModule);
      LOGGER.trace("CustomTab Factory - New Module: " + oldModule);
      if (module == newModule) {
        controlBox.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
        LOGGER.trace("STYLE SET");
      }
      if (module == oldModule) {
        // switch from this to other customTab
        controlBox.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
      }
    };

    workbench.activeModuleProperty().addListener(activeCustomTabListener);
  }
}
