package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.Module;
import java.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;

public class ToolBarPresenter implements Presenter {
  private final WorkbenchFxModel model;
  private final ToolBarView view;

  /**
   * The Constructor of the {@code ToolBarPresenter}.
   */
  public ToolBarPresenter(WorkbenchFxModel model, ToolBarView view) {
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
        if (c.wasPermutated()) {
          for (int i = c.getFrom(); i < c.getTo(); ++i) {
            System.out.println("Permutate");
          }
        } else if (c.wasUpdated()) {
          System.out.println("Update Item");
        } else {
          for (Module module : c.getRemoved()) {
            System.out.println("Remove");
            // +1 because of home
            view.getChildren().remove(c.getFrom() + 1);
          }
          for (Module module : c.getAddedSubList()) {
            System.out.println("Add");
            if (!Objects.isNull(module)) {
              Node tabControl = model.getTab(module);
              tabControl.getStyleClass().add("tabControl");
              view.getChildren().add(tabControl);
              tabControl.requestFocus();
            }
          }
        }
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
