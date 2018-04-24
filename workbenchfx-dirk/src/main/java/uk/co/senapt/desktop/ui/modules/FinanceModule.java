package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class FinanceModule extends ShellModule {

    public FinanceModule() {
        super("Finance");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-inactive-finance";
    }

    @Override
    protected String getIconClassBaseName() {
        return "uk/co/senapt/desktop/ui/modules/icons/finance";
    }
}
