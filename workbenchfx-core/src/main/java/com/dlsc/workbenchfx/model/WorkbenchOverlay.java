package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;

/**
 * TODO.
 */
public final class WorkbenchOverlay {
  
  private final Region overlay;
  private final GlassPane glassPane;

  private final TranslateTransition animationStart;
  private final TranslateTransition animationEnd;
  
  private boolean initialized = false;

  private final ObjectProperty<EventHandler<Event>> onInitialized =
      new SimpleObjectProperty<>(this, "onInitialized");

  /**
   * TODO.
   *
   * @param overlay TODO
   * @param glassPane TODO
   */
  public WorkbenchOverlay(Region overlay, GlassPane glassPane) {
    this(overlay, glassPane, null, null);
  }

  /**
   * TODO.
   *
   * @param overlay TODO
   * @param glassPane TODO
   * @param animationStart TODO
   * @param end TODO
   */
  public WorkbenchOverlay(Region overlay, GlassPane glassPane,
                          TranslateTransition animationStart, TranslateTransition end) {
    this.overlay = overlay;
    this.glassPane = glassPane;
    this.animationStart = animationStart;
    this.animationEnd = end;

    setupInitializedListeners(overlay);
  }

  private void setupInitializedListeners(Region overlay) {
    overlay.widthProperty().addListener(observable -> initialize(overlay));
    overlay.heightProperty().addListener(observable -> initialize(overlay));
  }

  private void initialize(Region overlay) {
    if (!isInitialized() && overlay.getWidth() > 0 && overlay.getHeight() > 0) {
      setInitialized(true);
      if (!Objects.isNull(getOnInitialized())) {
        getOnInitialized().handle(new Event(overlay, overlay, Event.ANY));
      }
    }
  }

  public final Region getOverlay() {
    return overlay;
  }

  public final GlassPane getGlassPane() {
    return glassPane;
  }

  /**
   * TODO.
   *
   * @return TODO
   */
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
    return initialized;
  }

  public final void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  /**
   * TODO.
   *
   * @return
   */
  public final ObjectProperty<EventHandler<Event>> onInitializedProperty() {
    return onInitialized;
  }

  public final void setOnInitialized(EventHandler<Event> value) {
    onInitialized.set(value);
  }

  public final EventHandler<Event> getOnInitialized() {
    return onInitialized.get();
  }
}
