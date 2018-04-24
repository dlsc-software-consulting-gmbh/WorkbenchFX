package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.module.Module;
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
    // When the List of the currently open modules is changed, the view is updated.
    model.getOpenModules().addListener((ListChangeListener<? super Module>) c -> {
      while (c.next()) {
        if (c.wasRemoved()) {
          for (Module module : c.getRemoved()) {
            LOGGER.debug("Module " + module.getName() + " closed");
            view.removeTab(c.getFrom());
          }
        }
        if (c.wasAdded()) {
          for (Module module : c.getAddedSubList()) {
            LOGGER.debug("Module " + module.getName() + " opened");
            Node tabControl = model.getTab(module);
//            tabControl.getStyleClass().add("active-tab");
            view.addTab(tabControl);
            tabControl.requestFocus();
          }
        }
      }
    });

//    model.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
//      if (Objects.isNull(oldModule)) {
//        // Home is the old value
//        System.out.println("REMOVED HOME");
//        view.homeBtn.getStyleClass().remove("active-tab");
//      } else {
//        // Some other is the old module
//        Node oldTab = model.getTab(oldModule);
//        view.tabBox.getChildren().forEach(tab -> {
//          if (Objects.equals(oldTab, tab)) {
//            tab.getStyleClass().remove("active-tab");
//            System.out.println("REMOVED " + oldModule.getName());
//          }
//        });
//      }
//
//      if (Objects.isNull(newModule)) {
//        // Home is the new value
//        view.homeBtn.getStyleClass().add("active-tab");
//        System.out.println("ADDED HOME");
//      } else {
//        // Some other is the new module
//        Node newTab = model.getTab(newModule);
//        view.tabBox.getChildren().forEach(tab -> {
//          if (Objects.equals(newTab, tab)) {
//            tab.getStyleClass().add("active-tab");
//            System.out.println("ADDED " + newModule.getName());
//          }
//        });
//      }
//
//    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }

}
