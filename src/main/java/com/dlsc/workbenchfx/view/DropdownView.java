package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class DropdownView extends HBox implements View {
  private final WorkbenchFx model;
  private final DoubleProperty height = new SimpleDoubleProperty(50);

  // TODO: add javadoc comment
  public DropdownView(WorkbenchFx model) {
    this.model = model;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("dropdown");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
//    setMouseTransparent(true);

    model.getDropdowns().forEach(dropdown -> {
      getChildren().add(dropdown);
//      dropdown.setMouseTransparent(false);
//      if (height.get() > dropdown.getHeight()) {
//        height.set(dropdown.getHeight());
//      }
    });
//    maxHeightProperty().bind(height);
//    setAlignment(Pos.TOP_RIGHT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

  // TODO: add javadoc comment
  public void removeDropdown(int index) {
    getChildren().remove(index);
  }

  // TODO: add javadoc comment
  public void addDropdown(Dropdown dropdown) {
    getChildren().add(dropdown);
  }
}
