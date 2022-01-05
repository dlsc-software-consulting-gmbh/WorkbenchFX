module com.dlsc.workbenchfx.core {
    requires com.google.common;
    requires javafx.controls;
    requires org.slf4j;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    exports com.dlsc.workbenchfx;
    exports com.dlsc.workbenchfx.model;
    exports com.dlsc.workbenchfx.util;
    exports com.dlsc.workbenchfx.view;
    exports com.dlsc.workbenchfx.view.controls;
    exports com.dlsc.workbenchfx.view.controls.dialog;
    exports com.dlsc.workbenchfx.view.controls.module;
    exports com.dlsc.workbenchfx.view.controls.selectionstrip;
}
