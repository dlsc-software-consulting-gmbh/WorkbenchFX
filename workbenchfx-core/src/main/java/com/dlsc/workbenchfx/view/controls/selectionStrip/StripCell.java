package com.dlsc.workbenchfx.view.controls.selectionStrip;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.Label;

public class StripCell<T> extends Label {

  private final static PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

  private final InvalidationListener selectionListener = it -> updateSelection();

  private final WeakInvalidationListener weakSelectionListener = new WeakInvalidationListener(
      selectionListener);

  public StripCell() {
    getStyleClass().add("strip-cell");
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);

    selectionStripProperty().addListener((it, oldStrip, newStrip) -> {
      if (oldStrip != null) {
        oldStrip.selectedItemProperty().removeListener(weakSelectionListener);
      }

      if (newStrip != null) {
        newStrip.selectedItemProperty().addListener(weakSelectionListener);
        updateSelection();
      }
    });

    itemProperty().addListener(it -> {
      setText(getItem() == null ? "" : getItem().toString());
      updateSelection();
    });
  }

  private void updateSelection() {
    final T selectedItem = getSelectionStrip().getSelectedItem();
    if (selectedItem == getItem()) {
      setSelected(true);
    } else {
      setSelected(false);
    }
  }

  private final ObjectProperty<SelectionStrip<T>> selectionStrip = new SimpleObjectProperty<>(this,
      "selectionStrip");

  public final ObjectProperty<SelectionStrip<T>> selectionStripProperty() {
    return selectionStrip;
  }

  public final SelectionStrip<T> getSelectionStrip() {
    return selectionStrip.get();
  }

  public final void setSelectionStrip(SelectionStrip<T> selectionStrip) {
    this.selectionStrip.set(selectionStrip);
  }

  private BooleanProperty selected;

  public final void setSelected(boolean value) {
    selectedProperty().set(value);
  }

  public final boolean isSelected() {
    return selected != null && selected.get();
  }

  public final BooleanProperty selectedProperty() {
    if (selected == null) {
      selected = new BooleanPropertyBase() {
        @Override
        protected void invalidated() {
          final boolean selected = get();
          pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, selected);
          notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
        }

        @Override
        public Object getBean() {
          return StripCell.this;
        }

        @Override
        public String getName() {
          return "selected";
        }
      };
    }
    return selected;
  }

  private final ObjectProperty<T> item = new SimpleObjectProperty<>(this, "item");

  public final T getItem() {
    return item.get();
  }

  public final ObjectProperty<T> itemProperty() {
    return item;
  }

  public final void setItem(T item) {
    this.item.set(item);
  }
}
