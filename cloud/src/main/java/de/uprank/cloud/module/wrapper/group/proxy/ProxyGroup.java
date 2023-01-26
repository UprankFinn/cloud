package de.uprank.cloud.module.wrapper.group.proxy;

import de.uprank.cloud.module.wrapper.group.proxy.template.Template;

import java.util.ArrayList;
import java.util.List;

public class ProxyGroup {

    private final String name;
    private final String wrapper;
    private final String version;
    private final List<Template> templates;

    public ProxyGroup(String name, String wrapper, String version) {
        this.name = name;
        this.wrapper = wrapper;
        this.version = version;
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
