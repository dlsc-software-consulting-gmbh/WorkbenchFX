package com.dlsc.workbenchfx.overlay;

import com.dlsc.workbenchfx.WorkbenchFx;
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

}
