package com.dlsc.workbenchfx.view.controls.selectionstrip;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;

public class TabCell extends StripCell<WorkbenchModule> {

  private static final String FIRST_CHILD = "first-child";

  /**
   * Constructs a new {@link TabCell}.
   */
  public TabCell() {
    super();
    itemProperty().addListener(it -> {
      // Remove text which was set in the listener of StripCell
      setText("");
      // Create Tab
      Workbench workbench = getItem().getWorkbench();
      Tab tab = workbench.getTabFactory().call(workbench);
      tab.setModule(getItem());
      setGraphic(tab);

      /*
        To remove the background-insets from this cell.
        Otherwise the SelectionStrip's end would cut off the side.
       */
      if (getSelectionStrip().getItems().get(0).equals(getItem())) {
        getStyleClass().add(FIRST_CHILD);
      }
    });
  }
}
