package com.dlsc.workbenchfx.view.controls.selectionStrip;

import com.dlsc.workbenchfx.view.controls.module.Tab;

public class TabCell extends StripCell<Tab> {
  public TabCell() {
    super();
    itemProperty().addListener(it -> {
      setText("");
      setGraphic(getItem());
    });

    selectedProperty().addListener((observable, oldSelected, newSelected) -> {
      if (newSelected && getItem() != null) {
        getItem().open();
      }
    });
  }
}
