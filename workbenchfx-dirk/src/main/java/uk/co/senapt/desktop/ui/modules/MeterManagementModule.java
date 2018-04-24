package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class MeterManagementModule extends ShellModule {

    public MeterManagementModule() {
        super("Meter Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-meter-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "meter-management";
    }
}
