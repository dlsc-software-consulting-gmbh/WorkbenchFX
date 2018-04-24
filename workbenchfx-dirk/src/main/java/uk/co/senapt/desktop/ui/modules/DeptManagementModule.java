package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class DeptManagementModule extends ShellModule {

    public DeptManagementModule() {
        super("Debt Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-inactive-debt-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "uk/co/senapt/desktop/ui/modules/icons/debt-management";
    }
}
