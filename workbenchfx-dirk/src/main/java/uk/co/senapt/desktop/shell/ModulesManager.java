package uk.co.senapt.desktop.shell;

import uk.co.senapt.desktop.shell.util.LoggingDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Created by lemmi on 25.04.17.
 */
public final class ModulesManager {

    private final static ModulesManager instance = new ModulesManager();

    private List<ShellModule> modules = new ArrayList<>();

    public static final ModulesManager getInstance() {
        return instance;
    }

    public ModulesManager() {
        final ServiceLoader<ShellModule> loader = ServiceLoader.load(ShellModule.class);
        loader.forEach(mod -> LoggingDomain.CONFIG.info("registered module: " + mod.getName() + ", class = " + mod.getClass().getName()));
        loader.forEach(mod -> modules.add(mod));
    }

    public List<ShellModule> getModules() {
        return modules;
    }

    public Optional<ShellModule> getModule(String name) {
        return modules.stream().filter(mod -> mod.getName().equals(name)).findFirst();
    }
}
