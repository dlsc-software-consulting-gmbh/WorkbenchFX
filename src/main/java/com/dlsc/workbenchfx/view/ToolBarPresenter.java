package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.module.Module;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToolBarPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(ToolBarPresenter.class.getName());
  private final WorkbenchFx model;
  private final ToolBarView view;

  // Strong reference to prevent garbage collection
  private ObservableList<Module> openModules;
  private final ObservableList<MenuItem> navigationDrawerItems;

  /**
   * Creates a new {@link ToolBarPresenter} object for a corresponding {@link ToolBarView}.
   */
  public ToolBarPresenter(WorkbenchFx model, ToolBarView view) {
    this.model = model;
    this.view = view;
    openModules = model.getOpenModules();
    navigationDrawerItems = model.getNavigationDrawerItems();
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    // initially create all tabs
    model.getOpenModules().forEach(module -> view.getChildren().add(model.getTab(module)));

    // only add the menu button, if there is at least one navigation drawer item
    if (model.getNavigationDrawerItems().size() > 0) {
      view.addMenuButton();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    // When the home button is clicked, the view changes
    view.homeBtn.setOnAction(event -> model.openHomeScreen());
    // When the menu button is clicked, the navigation drawer gets shown
    view.menuBtn.setOnAction(event -> model.showOverlay(model.getNavigationDrawer(), true));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    // When the List of the currently open modules is changed, the view is updated.
    openModules.addListener((ListChangeListener<? super Module>) c -> {
      while (c.next()) {
        if (c.wasRemoved()) {
          for (Module module : c.getRemoved()) {
            LOGGER.debug("Module " + module + " closed");
            view.removeTab(c.getFrom());
          }
        }
        if (c.wasAdded()) {
          for (Module module : c.getAddedSubList()) {
            LOGGER.debug("Module " + module + " opened");
            Node tabControl = model.getTab(module);
            view.addTab(tabControl);
            tabControl.requestFocus();
          }
        }
      }
    });

    model.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
      if (Objects.isNull(oldModule)) {
        // Home is the old value
        view.homeBtn.getStyleClass().remove(STYLE_CLASS_ACTIVE_TAB);
      }
      if (Objects.isNull(newModule)) {
        // Home is the new value
        view.homeBtn.getStyleClass().add(STYLE_CLASS_ACTIVE_TAB);
      }
    });

    // makes sure the menu button is only being displayed if there are navigation drawer items
    navigationDrawerItems.addListener(
        (InvalidationListener)
            observable -> {
              if (navigationDrawerItems.size() == 0) {
                view.removeMenuButton();
              } else {
                view.addMenuButton();
              }
            });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
  }

}
