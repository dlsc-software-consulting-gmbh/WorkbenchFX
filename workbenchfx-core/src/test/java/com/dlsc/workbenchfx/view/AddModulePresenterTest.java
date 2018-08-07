package com.dlsc.workbenchfx.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.module.Page;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.PseudoClass;
import javafx.util.Callback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class AddModulePresenterTest extends ApplicationTest {

  private AddModulePresenter addModulePresenter;
  private Workbench mockBench;
  private AddModuleView mockView;
  private Callback<Workbench, Page> mockCall;

  private static final PseudoClass ONE_PAGE_STATE = PseudoClass.getPseudoClass("one-page");
  private final IntegerProperty amountOfPages = new SimpleIntegerProperty(1);

  @BeforeEach
  void setup() {
    mockCall = mock(Callback.class);
    when(mockCall.call(any())).thenReturn(null);

    mockBench = mock(Workbench.class);

    when(mockBench.getAmountOfPages()).thenReturn(1);
    when(mockBench.amountOfPagesProperty()).thenReturn(amountOfPages);
    when(mockBench.getPageFactory()).thenReturn(mockCall);

    mockView = mock(AddModuleView.class);
  }

  @Test
  void testCtor() {
    addModulePresenter = new AddModulePresenter(mockBench, mockView);

    verify(mockView).setPageCount(1);
    verify(mockView).pseudoClassStateChanged(ONE_PAGE_STATE, true);
    verify(mockView).setPageFactory(any());
    verify(mockView).setMaxPageIndicatorCount(Integer.MAX_VALUE);
  }

  @Test
  void testAmountOfPagesListener() {
    addModulePresenter = new AddModulePresenter(mockBench, mockView);

    amountOfPages.setValue(2); // Change from 1 to 2 pages
    verify(mockView).setPageCount(2);
    verify(mockView).pseudoClassStateChanged(ONE_PAGE_STATE, false);

    reset(mockView); // Otherwise, the call before would still count and two interactions are logged
    amountOfPages.setValue(1); // Change from 2 to 1 pages
    verify(mockView).setPageCount(1);
    verify(mockView).pseudoClassStateChanged(ONE_PAGE_STATE, true);
  }
}
