package com.dlsc.workbenchfx.view.controls;

import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

  private final BooleanProperty hide = new SimpleBooleanProperty(this, "hide", true);

  /**
   * Creates a {@link GlassPane} object and fully initializes it.
   */
  public GlassPane() {
    getStyleClass().add("glass-pane");

    setMouseTransparent(false);
    setVisible(false);

    hideProperty().addListener((observable, oldHide, newHide) -> {
      // don't do anything if the state hasn't changed
      if (oldHide.equals(newHide)) {
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

  public final BooleanProperty hideProperty() {
    return hide;
  }

  public final boolean isHide() {
    return hide.get();
  }

  public final void setHide(boolean hide) {
    this.hide.set(hide);
  }
}
