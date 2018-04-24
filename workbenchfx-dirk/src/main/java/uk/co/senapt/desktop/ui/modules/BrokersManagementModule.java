package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class BrokersManagementModule extends ShellModule {

    public BrokersManagementModule() {
        super("Brokers Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-inactive-brokers-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "brokers-management";
    }
}
