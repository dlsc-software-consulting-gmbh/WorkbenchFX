package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Page;
import javafx.css.PseudoClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the presenter of the corresponding {@link AddModuleView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class AddModulePresenter extends Presenter {

  private static final Logger LOGGER =
      LogManager.getLogger(AddModulePresenter.class.getName());

  private static final PseudoClass ONE_PAGE_STATE = new PseudoClass() {
    @Override
    public String getPseudoClassName() {
      return "one-page";
    }
  };
  private final Workbench model;
  private final AddModuleView view;

  /**
   * Creates a new {@link AddModulePresenter} object for a corresponding {@link AddModuleView}.
   *
   * @param model the workbench, holding all data
   * @param view the corresponding {@link AddModuleView}
   */
  public AddModulePresenter(Workbench model, AddModuleView view) {
    this.model = model;
    this.view = view;
    init();
    view.pagination.pseudoClassStateChanged(
        ONE_PAGE_STATE, view.pagination.getPageCount() == 1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    view.pagination.setPageCount(model.getAmountOfPages());
    view.pagination.setPageFactory(pageIndex -> {
      Page page = model.getPageFactory().call(model);
      page.setPageIndex(pageIndex);
      return page;
    });
    view.pagination.setMaxPageIndicatorCount(Integer.MAX_VALUE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    model.amountOfPagesProperty().addListener((observable, oldPageCount, newPageCount) -> {
      view.pagination.setPageCount(newPageCount.intValue());
      view.pagination.pseudoClassStateChanged(
          ONE_PAGE_STATE, newPageCount.intValue() == 1);
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }
}
