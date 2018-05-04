package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.overlay.Overlay;
import com.google.common.collect.HashBiMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Represents a black transparent overlay, which hides all currently shown overlays, when it is
 * being clicked.
 *
 * @author Dirk Lemmermann
 * @author François Martin
 * @author Marco Sanfratello
 */
public class GlassPane extends StackPane {

  /**
   * Creates a {@link GlassPane} object and fully initializes it.
   */
  public GlassPane() {
    getStyleClass().add("glass-pane");

    setMouseTransparent(false);
    setVisible(false);

    hideProperty().addListener((observable, oldHide, newHide) -> {
      // don't do anything if the state hasn't changed
      if (oldHide == newHide) {
        return;
      }
      setVisible(true);

      FadeTransition fadeTransition = new FadeTransition();
      fadeTransition.setDuration(Duration.millis(200));
      fadeTransition.setNode(this);
      fadeTransition.setFromValue(this.isHide() ? .5 : 0);
      fadeTransition.setToValue(this.isHide() ? 0 : .5);
      fadeTransition.setOnFinished(evt -> {
        if (isHide()) {
          setVisible(false);
        }
      });
      fadeTransition.play();
    });
  }

  private final BooleanProperty hide = new SimpleBooleanProperty(this, "hide");

  public final BooleanProperty hideProperty() {
    return hide;
  }

  public final void setHide(boolean hide) {
    this.hide.set(hide);
  }

  public final boolean isHide() {
    return hide.get();
  }
}
