package com.dlsc.workbenchfx.view.controls.selectionstrip;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.beans.value.ChangeListener;

public class TabCell extends StripCell<WorkbenchModule> {

  private final String previousCell = "previous-cell";
  private final ChangeListener<WorkbenchModule> previousCellListener =
      (observableValue, oldModule, newModule) -> {
        int cellIndex = getSelectionStrip().getItems().indexOf(getItem());
        if (cellIndex + 1 == getSelectionStrip().getItems().indexOf(oldModule)) {
          getStyleClass().remove(previousCell);
        }
        if (cellIndex + 1 == getSelectionStrip().getItems().indexOf(newModule)) {
          getStyleClass().add(previousCell);
        }
      };

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
      setId(
          "tab-" +
              getItem()
                  .toString()
                  .replace(" ", "-")
                  .toLowerCase()
      );
    });

    selectionStripProperty().addListener((observable, oldStrip, newStrip) -> {
      if (oldStrip != null) {
        oldStrip.selectedItemProperty().removeListener(previousCellListener);
      }

      if (newStrip != null) {
        newStrip.selectedItemProperty().addListener(previousCellListener);
      }
    });
  }
}
