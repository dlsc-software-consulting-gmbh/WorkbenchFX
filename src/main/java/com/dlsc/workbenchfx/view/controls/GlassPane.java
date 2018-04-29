package com.dlsc.workbenchfx.view.controls;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GlassPane extends StackPane {

  public GlassPane(WorkbenchFx workbench) {
    getStyleClass().add("glass-pane");

    setMouseTransparent(false);
    setOnMouseClicked(evt -> workbench.hideAllOverlays());
    setVisible(false);

    hideProperty()
        .addListener(
            it -> {
              setVisible(true);

              FadeTransition fadeTransition = new FadeTransition();
              fadeTransition.setDuration(Duration.millis(200));
              fadeTransition.setNode(this);
              fadeTransition.setFromValue(this.isHide() ? .5 : 0);
              fadeTransition.setToValue(this.isHide() ? 0 : .5);
              fadeTransition.setOnFinished(
                  evt -> {
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
