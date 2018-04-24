package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class SalesManagementModule extends ShellModule {

    public SalesManagementModule() {
        super("Sales Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-inactive-sales-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "sales-management";
    }
}
