package com.dlsc.workbenchfx.model.module;

import com.dlsc.workbenchfx.WorkbenchFx;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 * Represents the base for a module, to be displayed in WorkbenchFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public interface Module {

    /**
     * Returns the node to be displayed in the toolbar for the tab of this module.
     */
    Node getTab();

    /**
     * Returns the node of the tile to be displayed for this module in the home screen.
     */
    Node getTile();

    // Lifecycle
    /**
     * Gets called when the module is being opened from the overview for the first time.
     * @param workbench the calling workbench object
     * @return content to be displayed in this module
     */
    Node init(WorkbenchFx workbench);

    /**
     * Gets called whenever the currently displayed content is being switched to this module.
     * @implNote if a module is being opened from the overview for the first time, it will
     * get initialized first by calling init(), afterwards activate() will be called.
     */
    void activate();

    /**
     * Gets called whenever this module is the currently displayed content and the content is being
     * switched to another module.
     * @implNote Assuming Module 1 and Module 2, with both being already initialized and Module 1
     * being the currently displayed content.
     * When switching the content to Module 2, deactivate() gets called on Module 1,
     * followed by a call of activate() on Module 2.
     */
    void deactivate();

    /**
     * Gets called when this module is explicitly being closed by the user in the toolbar.
     */
    void destroy();



}
