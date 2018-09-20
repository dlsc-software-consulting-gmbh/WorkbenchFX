module com.dlsc.workbenchfx.core {
    requires javafx.controls;
    requires javafx.swing;
    requires org.apache.logging.log4j;
    requires com.google.common;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.materialdesignicons;

    exports com.dlsc.workbenchfx;
    exports com.dlsc.workbenchfx.model;
    exports com.dlsc.workbenchfx.util;
    exports com.dlsc.workbenchfx.view;
    exports com.dlsc.workbenchfx.view.controls;
    exports com.dlsc.workbenchfx.view.controls.dialog;
    exports com.dlsc.workbenchfx.view.controls.module;
    exports com.dlsc.workbenchfx.view.controls.selectionstrip;
}