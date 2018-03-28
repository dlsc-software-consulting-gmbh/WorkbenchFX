module crm.desktop.ui.shell {

    requires java.logging;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.web; // because of scenic view web view requirements

    requires controlsfx;

    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.fxml;
    requires org.slf4j;

    exports uk.co.senapt.desktop.shell;
    exports uk.co.senapt.desktop.shell.model;
    exports uk.co.senapt.desktop.shell.util;

    opens uk.co.senapt.desktop.shell;

    uses uk.co.senapt.desktop.shell.ShellModule;
}