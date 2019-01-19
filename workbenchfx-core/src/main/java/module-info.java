module com.dlsc.workbenchfx.core {
    requires com.google.common;
    requires javafx.controls;
    requires de.jensd.fx.fontawesomefx.fontawesome;
    requires de.jensd.fx.fontawesomefx.materialdesignicons;
    requires slf4j.api;

    exports com.dlsc.workbenchfx;
    exports com.dlsc.workbenchfx.model;
    exports com.dlsc.workbenchfx.util;
    exports com.dlsc.workbenchfx.view;
    exports com.dlsc.workbenchfx.view.controls;
    exports com.dlsc.workbenchfx.view.controls.dialog;
    exports com.dlsc.workbenchfx.view.controls.module;
    exports com.dlsc.workbenchfx.view.controls.selectionstrip;
}
