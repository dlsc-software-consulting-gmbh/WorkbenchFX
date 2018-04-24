package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class ReportsModule extends ShellModule {

    public ReportsModule() {
        super("Reports");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-reports";
    }

    @Override
    protected String getIconClassBaseName() {
        return "uk/co/senapt/desktop/ui/modules/icons/reports";
    }
}
