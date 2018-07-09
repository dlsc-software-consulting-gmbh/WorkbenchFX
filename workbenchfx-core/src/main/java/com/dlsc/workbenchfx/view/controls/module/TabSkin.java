package com.dlsc.workbenchfx.view.controls.module;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the skin of the corresponding {@link Tab}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class TabSkin extends SkinBase<Tab> {
  private static final Logger LOGGER = LogManager.getLogger(TabSkin.class.getName());

  private final ReadOnlyObjectProperty<WorkbenchModule> module;

  private HBox controlBox;
  private Button closeBtn;
  private FontAwesomeIconView closeIconView;

  private Label nameLbl;

  private final ReadOnlyBooleanProperty activeTab;
  private final ReadOnlyStringProperty name;
  private final ReadOnlyObjectProperty<Node> icon;

  /**
   * Creates a new {@link TabSkin} object for a corresponding {@link Tab}.
   *
   * @param tab the {@link Tab} for which this Skin is created
   */
  public TabSkin(Tab tab) {
    super(tab);
    module = tab.moduleProperty();
    activeTab = tab.activeTabProperty();
    name = tab.nameProperty();
    icon = tab.iconProperty();

    initializeParts();
    layoutParts();
    setupBindings();
    setupEventHandlers();
    setupValueChangedListeners();

    updateIcon();

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

  private void setupBindings() {
    nameLbl.textProperty().bind(name);
  }

  private void setupEventHandlers() {
    closeBtn.setOnAction(e -> getSkinnable().close());
    controlBox.setOnMouseClicked(e -> getSkinnable().open());
  }

  private void setupValueChangedListeners() {
    // add or remove "active tab" style class, depending on state
    activeTab.addListener((observable, wasActive, isActive) -> {
      LOGGER.trace("Tab Factory - Was active: " + wasActive);
      LOGGER.trace("Tab Factory - Is active: " + isActive);
      if (isActive) {
        controlBox.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
        LOGGER.trace("STYLE SET");
      } else {
        // switch from this to other tab
        controlBox.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
      }
    });

    // handle icon changes
    icon.addListener((observable, oldIcon, newIcon) -> {
      if (oldIcon != newIcon) {
        updateIcon();
      }
    });
  }

  private void updateIcon() {
    Node iconNode = icon.get();
    ObservableList<Node> children = controlBox.getChildren();
    children.remove(0);
    children.add(0, iconNode);
    iconNode.getStyleClass().add("tab-icon");
  }
}
