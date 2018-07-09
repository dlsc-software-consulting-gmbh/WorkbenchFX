package com.dlsc.workbenchfx.view.controls.selectionStrip;

import com.dlsc.workbenchfx.view.controls.module.Tab;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;

public class TabCell extends StripCell<Tab> {

  private final ChangeListener<Boolean> activeTabListener = (observable, oldActive, newActive) -> {
    setSelected(newActive);
  };

  public TabCell() {
    super();
    itemProperty().addListener(it -> {
      setText("");
      setGraphic(getItem());
    });

//    selectedProperty().bind(getItem().activeTabProperty());



    selectedProperty().bind(Bindings.select(itemProperty(), "activeTab"));



//        Bindings.createBooleanBinding(() -> {
//          if (getItem() == null) {
//            return false;
//          }
//          return getItem().isActiveTab();
//        }, itemProperty()));

    itemProperty().addListener((observable, oldItem, newItem) -> {
      System.out.println("olditem: " + oldItem);
      System.out.println("newitem: " + newItem);
      if (oldItem != null) {
        oldItem.activeTabProperty().removeListener(activeTabListener);
      }
      if (newItem != null) {
        newItem.activeTabProperty().addListener(activeTabListener);
//        setSelected(true);
      }
    });
  }
}
