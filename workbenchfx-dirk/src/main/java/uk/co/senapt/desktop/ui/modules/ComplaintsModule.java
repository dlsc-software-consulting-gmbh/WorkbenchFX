package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class ComplaintsModule extends ShellModule {

    public ComplaintsModule() {
        super("Complaints");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-complaints";
    }

    @Override
    protected String getIconClassBaseName() {
        return "uk/co/senapt/desktop/ui/modules/icons/complaints";
    }
}
