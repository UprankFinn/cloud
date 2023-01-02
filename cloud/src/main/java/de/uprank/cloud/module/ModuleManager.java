package de.uprank.cloud.module;

import joptsimple.OptionParser;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {

    private final Map<String, Module> modules;
    private OptionParser optionParser;

    public ModuleManager() {
        this.modules = new HashMap<>();
    }

    public void addModule(String name, Module module) {
        this.modules.put(name, module);
    }

    public void removeModule(String name) {
        this.modules.remove(name);
    }

    public Map<String, Module> getModules() {
        return modules;
    }

    public OptionParser getOptionParser() {
        return optionParser;
    }

}
