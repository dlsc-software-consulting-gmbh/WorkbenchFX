package com.dlsc.workbenchfx.overlay;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.view.controls.GlassPane;
import javafx.scene.Node;

/**
 * Represents an overlay, which can be displayed stacked on top of the view of WorkbenchFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public interface Overlay {

  /**
   * Gets called when the overlay is being loaded upon initialization of {@link WorkbenchFx}.
   *
   * @param workbench the calling workbench object
   * @return content to be displayed in this overlay
   */
  Node init(WorkbenchFx workbench);

  /**
   * Returns whether this overlay is blocking or not.
   *
   * @implNote Each overlay will be opened with a black transparent {@link GlassPane} in the
   *           background.
   *           In case of a non-blocking overlay (return false), clicking outside of this overlay
   *           will cause it to get hidden, together with the {@link GlassPane}.
   *           In case of a blocking overlay (return true), clicking outside of this overlay
   *           will do nothing - the interface implementor has to explicitly hide this overlay
   *           by themself.
   */
  boolean isBlocking();

}
