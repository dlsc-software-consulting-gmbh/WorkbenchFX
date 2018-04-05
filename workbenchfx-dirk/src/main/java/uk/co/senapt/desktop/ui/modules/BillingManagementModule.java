package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class BillingManagementModule extends ShellModule {

    public BillingManagementModule() {
        super("Billing Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-billing-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "billing-management";
    }
}
