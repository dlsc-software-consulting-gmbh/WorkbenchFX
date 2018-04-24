package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class TarrifManagementModule extends ShellModule {

    public TarrifManagementModule() {
        super("Tariff Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-inactive-tarrifs-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "tariff-management";
    }
}
