package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class TemplatesModule extends ShellModule {

    public TemplatesModule() {
        super("Templates");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-templates";
    }

    @Override
    protected String getIconClassBaseName() {
        return "uk/co/senapt/desktop/ui/modules/icons/templates";
    }
}
