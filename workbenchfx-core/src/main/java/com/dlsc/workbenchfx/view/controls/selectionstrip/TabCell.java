package com.dlsc.workbenchfx.view.controls.selectionstrip;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;

public class TabCell extends StripCell<WorkbenchModule> {

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
    });
  }
}
