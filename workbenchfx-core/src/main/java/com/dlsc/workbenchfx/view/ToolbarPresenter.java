package com.dlsc.workbenchfx.view;

import static com.dlsc.workbenchfx.Workbench.STYLE_CLASS_ACTIVE_ADD_BUTTON;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.selectionstrip.TabCell;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
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

  private final PseudoClass emptyState = new PseudoClass() {
    @Override
    public String getPseudoClassName() {
      return "empty";
    }
  };

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
    // Adds initially a menuButton if necessary (size of items > 0)
    setupMenuBtn();
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
  }

  private void setupMenuBtn() {
    LOGGER.trace("setupMenuBtn() called");
    view.removeMenuBtn(); // Remove the menuBtn

    if (navigationDrawerItems.size() != 0) { // If setting it is required
      if (view.workbenchToolbar.isEmpty()) { // If the workbenchToolbar is empty set it below
        LOGGER.trace("Put the button below into the bottomBox");
        view.addMenuBtnBottom();
      } else { // else put it into the topBox
        LOGGER.trace("Put it into the first position of toolbaritemsleft");
        view.addMenuBtnTop();
      }
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
    navigationDrawerItems.addListener((InvalidationListener) observable -> setupMenuBtn());
    // when the workbenchToolbar's emptyProperty changes, check the menuBtn's position
    view.workbenchToolbar.emptyProperty().addListener((observable, wasEmpty, isEmpty) -> {
      view.topBox.pseudoClassStateChanged(emptyState, isEmpty); // Change the pseudoclass
      setupMenuBtn(); // Define where to put the menuBtn
    });
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
    view.workbenchToolbar.toolbarControlsLeftProperty().bindContent(toolbarControlsLeft);
    view.workbenchToolbar.toolbarControlsRightProperty().bindContent(toolbarControlsRight);
  }
}
