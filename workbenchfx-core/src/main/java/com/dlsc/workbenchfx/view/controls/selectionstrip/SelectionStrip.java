package com.dlsc.workbenchfx.view.controls.selectionstrip;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.Duration;

public class SelectionStrip<T> extends Control {

  /**
   * Constructs a new {@link SelectionStrip}.
   */
  public SelectionStrip() {
    getStyleClass().add("selection-strip");

    setPrefWidth(400);
    setPrefHeight(50);

    setCellFactory(strip -> new StripCell<>());

    selectedItemProperty().addListener(it -> {
      if (getSelectedItem() != null && isAutoScrolling()) {
        scrollTo(getSelectedItem());
        requestLayout();
      }
    });
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new SelectionStripSkin<>(this);
  }

  @Override
  public String getUserAgentStylesheet() {
    return SelectionStrip.class.getResource("selection-strip.css").toExternalForm();
  }

  // Autoscrolling.

  private final BooleanProperty autoScrolling = new SimpleBooleanProperty(this, "autoScrolling",
      true);

  public final BooleanProperty autoScrollingProperty() {
    return autoScrolling;
  }

  public final boolean isAutoScrolling() {
    return autoScrolling.get();
  }

  public final void setAutoScrolling(boolean autoScrolling) {
    this.autoScrolling.set(autoScrolling);
  }

  // Animation support.

  private final BooleanProperty animateScrolling = new SimpleBooleanProperty(this,
      "animateScrolling", true);

  public final BooleanProperty animateScrollingProperty() {
    return animateScrolling;
  }

  public final boolean isAnimateScrolling() {
    return animateScrolling.get();
  }

  public final void setAnimateScrolling(boolean animateScrolling) {
    this.animateScrolling.set(animateScrolling);
  }

  // Animation duration support.

  private final ObjectProperty<Duration> animationDuration = new SimpleObjectProperty<>(this,
      "animationDuration", Duration
      .millis(200));

  public final ObjectProperty<Duration> animationDurationProperty() {
    return animationDuration;
  }

  public final Duration getAnimationDuration() {
    return animationDuration.get();
  }

  public final void setAnimationDuration(Duration animationDuration) {
    this.animationDuration.set(animationDuration);
  }

  // Selection model support.

  public final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>(this, "selectedItem");

  public final ObjectProperty<T> selectedItemProperty() {
    return selectedItem;
  }

  public final T getSelectedItem() {
    return selectedItemProperty().get();
  }

  public final void setSelectedItem(T selectedItem) {
    selectedItemProperty().set(selectedItem);
  }

  // items support

  private final ListProperty<T> items = new SimpleListProperty<>(this, "items",
      FXCollections.observableArrayList());

  public final ListProperty<T> itemsProperty() {
    return items;
  }

  public final ObservableList<T> getItems() {
    return items.get();
  }

  public final void setItems(ObservableList<T> items) {
    this.items.set(items);
  }

  // cell factory support

  private final ObjectProperty<Callback<SelectionStrip, StripCell<T>>> cellFactory
      = new SimpleObjectProperty<>(this, "cellFactory");

  public Callback<SelectionStrip, StripCell<T>> getCellFactory() {
    return cellFactory.get();
  }

  public ObjectProperty<Callback<SelectionStrip, StripCell<T>>> cellFactoryProperty() {
    return cellFactory;
  }

  public void setCellFactory(Callback<SelectionStrip, StripCell<T>> cellFactory) {
    this.cellFactory.set(cellFactory);
  }

  public void scrollTo(T item) {
    getProperties().put("scroll.to", item);
  }
}
