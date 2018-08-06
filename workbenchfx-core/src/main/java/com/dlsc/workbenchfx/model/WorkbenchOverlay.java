package com.dlsc.workbenchfx.model;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import java.util.Objects;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.layout.Region;

/**
 * Represents the model class of an overlay.
 *
 * <p>An overlay can be shown using:<br>
 * {@link Workbench#showOverlay(Region, boolean)}<br>
 * {@link Workbench#showDialog(WorkbenchDialog)}<br>
 * {@link Workbench#showDrawer(Region, Side)}
 *
 * @author François Martin
 * @author Marco Sanfratello
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
   * Initializes a new non-animated overlay.
   *
   * @param overlay to be shown as the main content node
   * @param glassPane to be shown in the background of the overlay
   */
  public WorkbenchOverlay(Region overlay, GlassPane glassPane) {
    this(overlay, glassPane, null, null);
  }

  /**
   * Initializes a new <b>animated</b> overlay.
   *
   * @param overlay to be shown as the main content node
   * @param glassPane to be shown in the background of the overlay
   * @param animationStart animation that should be played when the overlay is being shown
   * @param animationEnd animation that should be played when the overlay is being hidden
   */
  public WorkbenchOverlay(Region overlay, GlassPane glassPane,
                          TranslateTransition animationStart, TranslateTransition animationEnd) {
    this.overlay = overlay;
    this.glassPane = glassPane;
    this.animationStart = animationStart;
    this.animationEnd = animationEnd;

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
   * Returns whether or not this overlay is animated.
   *
   * @return whether or not this overlay is animated.
   * @implNote An overlay is being considered as animated when {@code animationStart} and
   *           {@code animationEnd} are not null. The defined animation will then be played when
   *           the overlay is being shown or hidden.
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

  private final void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  /**
   * Enables to define actions that should be performed when an overlay is initialized.
   *
   * @return the {@link ObjectProperty} of the {@link EventHandler} which will be triggered upon
   *         initialization
   * @implNote an overlay is being considered as initialized, when the {@code overlay} node has been
   *           added to the scene graph and a layout pass has been performed, which means the values
   *           of {@link Region#widthProperty()} and {@link Region#heightProperty()} are defined.
   *           This enables to initially set the position of an overlay using its size dynamically,
   *           for example before an animation is being performed for the first time.
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
