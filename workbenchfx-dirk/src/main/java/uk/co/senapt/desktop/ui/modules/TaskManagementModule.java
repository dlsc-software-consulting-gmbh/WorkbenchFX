package uk.co.senapt.desktop.ui.modules;

import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 18.08.17.
 */
public class TaskManagementModule extends ShellModule {

    public TaskManagementModule() {
        super("Task Management");
    }

    @Override
    public String getPrimaryColorStyleClass() {
        return "primary-color-inactive-task-management";
    }

    @Override
    protected String getIconClassBaseName() {
        return "task-management";
    }
}
