package uk.co.senapt.desktop.shell.util;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import uk.co.senapt.desktop.shell.ShellModule;

/**
 * Created by lemmi on 25.04.17.
 */
@Singleton
public final class ModulesManager {

    private final List<ShellModule> modules = new ArrayList<>();

    public ModulesManager() {
        ServiceLoader<ShellModule> loader = ServiceLoader.load(ShellModule.class);
        loader.forEach(mod -> LoggingDomain.CONFIG.info("registered module: " + mod.getName() + ", class = " + mod.getClass().getName()));
        loader.forEach(modules::add);
    }

    public List<? extends ShellModule> getModules() {
        return modules;
    }

    public ShellModule getCalendarModule() {
        return modules.stream().filter(m -> m.getName().equals("Calendar")).findFirst().orElse(null);
    }

    public boolean isCalendarModuleRegistered() {
        return getCalendarModule() != null;
    }

    public ShellModule getModule(Class<?> c) {
        final Optional<? extends ShellModule> first = getModules().stream().filter(m -> m.getClass() == c).findFirst();
        if (first.isPresent()) {
            return first.get();
        }

        throw new IllegalArgumentException("no module found of type: " + c.getName());
    }
}
