package com.dlsc.workbenchfx.view.controls.selectionstrip;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;

public class TabCell extends StripCell<WorkbenchModule> {

  private final String firstChild = "first-child";

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

      // Sets id with toString of module.
      // Adds 'tab-', replaces spaces with highfins and lowecases letters.
      // eg. Customer Management converts to tab-customer-management
      setId("tab-" + getItem().toString().replace(" ", "-").toLowerCase());

      /*
        To remove the background-insets from this cell.
        Otherwise the SelectionStrip's end would cut off the side.
       */
      if (getSelectionStrip().getItems().get(0).equals(getItem())) {
        getStyleClass().add(firstChild);
      }
    });
  }
}
