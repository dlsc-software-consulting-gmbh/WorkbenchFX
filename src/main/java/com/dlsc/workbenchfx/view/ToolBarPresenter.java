package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.WorkbenchFx.STYLE_CLASS_ACTIVE_TAB;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToolBarPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(ToolBarPresenter.class.getName());
  private final WorkbenchFx model;
  private final ToolBarView view;

  /**
   * Creates a new {@link ToolBarPresenter} object for a corresponding {@link ToolBarView}.
   */
  public ToolBarPresenter(WorkbenchFx model, ToolBarView view) {
    this.model = model;
    this.view = view;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    model.getOpenModules().forEach(module -> view.getChildren().add(model.getTab(module)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    // When the home button is clicked, the view changes
    view.homeBtn.setOnAction(event -> model.openHomeScreen());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    // When the List of the currently open toolBarControls is changed, the view is updated.
    model.getToolBarControls().addListener((ListChangeListener<? super Dropdown>) c -> {
      while (c.next()) {
        if (c.wasRemoved()) {
          for (Dropdown dropdown : c.getRemoved()) {
            LOGGER.debug("Dropdown " + dropdown + " removed");
            view.removeToolBarControl(c.getFrom());
          }
        }
        if (c.wasAdded()) {
          for (Dropdown dropdown : c.getAddedSubList()) {
            LOGGER.debug("Dropdown " + dropdown + " added");
            view.addToolBarControl(dropdown);
          }
        }
      }
    });

    // When the List of the currently open modules is changed, the view is updated.
    model.getOpenModules().addListener((ListChangeListener<? super Module>) c -> {
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }

}
