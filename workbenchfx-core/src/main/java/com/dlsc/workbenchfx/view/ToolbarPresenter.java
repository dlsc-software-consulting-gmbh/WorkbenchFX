package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_ADD_BUTTON;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.selectionstrip.TabCell;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the presenter of the corresponding {@link ToolbarView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToolbarPresenter extends Presenter {

  private static final Logger LOGGER =
      LogManager.getLogger(ToolbarPresenter.class.getName());
  private final Workbench model;
  private final ToolbarView view;

  // Strong reference to prevent garbage collection
  private final ObservableList<MenuItem> navigationDrawerItems;
  private final ObservableList<Node> toolbarControlsLeft;
  private final ObservableSet<Node> toolbarControlsRight;
  private final ObservableList<WorkbenchModule> openModules;

  /**
   * Creates a new {@link ToolbarPresenter} object for a corresponding {@link ToolbarView}.
   *
   * @param model the workbench, holding all data
   * @param view the corresponding {@link ToolbarView}
   */
  public ToolbarPresenter(Workbench model, ToolbarView view) {
    this.model = model;
    this.view = view;
    navigationDrawerItems = model.getNavigationDrawerItems();
    toolbarControlsLeft = model.getToolbarControlsLeft();
    toolbarControlsRight = model.getToolbarControlsRight();
    openModules = model.getOpenModules();
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.tabBar.setCellFactory(tab -> new TabCell());
    view.tabBar.getStylesheets().add(
        Workbench.class.getResource("css/selection-strip.css").toExternalForm()
    );

    view.addModuleBtn.requestFocus();

    // Adds a menuButton if necessary (size of items > 0)
    setMenuBtn();
  }

  private void setMenuBtn() {
    if (navigationDrawerItems.size() == 0) {
      // Remove menuBtn
      view.bottomBox.getChildren().remove(view.menuBtn);
      toolbarControlsLeft.remove(view.menuBtn);
    } else if (toolbarControlsLeft.size() + toolbarControlsRight.size() == 0) {
      // Put the button below into the bottomBox
      view.bottomBox.getChildren().add(0, view.menuBtn);
    } else {
      // Put it into the first position of toolbaritemsleft
      toolbarControlsLeft.add(0, view.menuBtn);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    // When the home button is clicked, the view changes
    view.addModuleBtn.setOnAction(event -> model.openHomeScreen());
    // When the menu button is clicked, the navigation drawer gets shown
    view.menuBtn.setOnAction(event -> model.showNavigationDrawer());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    model.activeModuleProperty().addListener((observable, oldModule, newModule) -> {
      if (Objects.isNull(oldModule)) {
        // AddModuleView is the old value
        view.addModuleBtn.getStyleClass().remove(STYLE_CLASS_ACTIVE_ADD_BUTTON);
      }
      if (Objects.isNull(newModule)) {
        // AddModuleView is the new value
        view.addModuleBtn.getStyleClass().add(STYLE_CLASS_ACTIVE_ADD_BUTTON);
      }
    });

    // makes sure the menu button is only being displayed if there are navigation drawer items
    navigationDrawerItems.addListener((InvalidationListener) observable -> setMenuBtn());

    view.getToolbarControl().emptyProperty().addListener((observable) -> setMenuBtn());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    // Binds content of the SelectionStrip to the Workbench content
    view.tabBar.itemsProperty().bindContent(openModules);
    view.tabBar.selectedItemProperty().bindBidirectional(model.activeModuleProperty());

    // Bind items from toolbar to the ones of the workbench
    view.getToolbarControl().toolbarControlsLeftProperty().bindContent(toolbarControlsLeft);
    view.getToolbarControl().toolbarControlsRightProperty().bindContent(toolbarControlsRight);
  }

}
