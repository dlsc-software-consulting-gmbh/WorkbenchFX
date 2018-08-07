package com.dlsc.workbenchfx.model

import com.dlsc.workbenchfx.view.controls.GlassPane
import javafx.animation.TranslateTransition
import javafx.scene.layout.Region
import spock.lang.Specification
import spock.lang.Unroll

class WorkbenchOverlaySpec extends Specification {
    void setup() {
    }

    void cleanup() {
    }

    @Unroll
    def "IsAnimated, with #startExists and #endExists"() {
        given:
        WorkbenchOverlay workbenchOverlay = new WorkbenchOverlay(overlay, glassPane, animationStart, animationEnd)

        expect:
        animated == workbenchOverlay.isAnimated()

        where:
        overlay      | glassPane       | animationStart            | animationEnd              || animated
        Mock(Region) | Mock(GlassPane) | null                      | null                      || false
        Mock(Region) | Mock(GlassPane) | new TranslateTransition() | null                      || false
        Mock(Region) | Mock(GlassPane) | null                      | new TranslateTransition() || false
        Mock(Region) | Mock(GlassPane) | new TranslateTransition() | new TranslateTransition() || true

        startExists = animationStart == null ? "start not defined" : "start defined"
        endExists = animationEnd == null ? "end not defined" : "end defined"
    }
}
