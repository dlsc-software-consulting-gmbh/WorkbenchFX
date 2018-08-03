package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;

public final class WorkbenchOverlay {
  
  private final Region overlay;
  private final GlassPane glassPane;

  private final TranslateTransition animationStart;
  private final TranslateTransition animationEnd;
  
  private final BooleanProperty initialized = new SimpleBooleanProperty();

  public WorkbenchOverlay(Region overlay, GlassPane glassPane, TranslateTransition animationStart, TranslateTransition end) {
    this.overlay = overlay;
    this.glassPane = glassPane;
    this.animationStart = animationStart;
    this.animationEnd = end;

    initialized.bind(
        overlay.widthProperty().greaterThan(0).or(overlay.heightProperty().greaterThan(0)));
  }

  public WorkbenchOverlay(Region overlay, GlassPane glassPane) {
    this(overlay, glassPane, null, null);
  }

  public final Region getOverlay() {
    return overlay;
  }

  public final GlassPane getGlassPane() {
    return glassPane;
  }

  public final boolean isAnimated() {
    return !Objects.isNull(animationStart) && !Objects.isNull(animationEnd);
  }

  public final TranslateTransition getAnimationStart() {
    return animationStart;
  }

  public final TranslateTransition getAnimationEnd() {
    return animationEnd;
  }

  public final boolean isInitialized() {
    return initialized.get();
  }

  public final ReadOnlyBooleanProperty initializedProperty() {
    return initialized;
  }

  private final void setInitialized(boolean initialized) {
    this.initialized.set(initialized);
  }
}
