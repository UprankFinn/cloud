package de.uprank.cloud.module.wrapper.group.server;

import de.uprank.cloud.module.wrapper.group.server.template.Template;

import java.util.ArrayList;
import java.util.List;

public class ServiceGroup {

    private final String name;
    private final String wrapper;
    private final String version;
    private final Boolean isFallBack;
    private final List<Template> templates;

    public ServiceGroup(String name, String wrapper, String version, Boolean isFallBack) {
        this.name = name;
        this.wrapper = wrapper;
        this.version = version;
        this.isFallBack = isFallBack;
        this.templates = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getVersion() {
        return version;
    }

    public Boolean getFallBack() {
        return isFallBack;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void addTemplate(Template template) {
        this.templates.add(template);
    }

    public void removeTemplate(Template template) {
        this.templates.remove(template);
    }

}
