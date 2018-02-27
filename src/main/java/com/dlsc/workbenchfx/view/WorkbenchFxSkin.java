package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.scene.control.SkinBase;

/**
 * Represents the default Skin for the WorkbenchFX control.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchFxSkin extends SkinBase<WorkbenchFx> {
    /**
     * Constructor for the standard WorkbenchFX skin.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected WorkbenchFxSkin(WorkbenchFx control) {
        super(control);
    }
}
