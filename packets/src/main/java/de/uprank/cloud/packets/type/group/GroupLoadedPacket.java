package de.uprank.cloud.packets.type.group;

import java.io.Serializable;
import java.util.List;

public class GroupLoadedPacket implements Serializable {

    private final String name;
    private final String wrapper;
    private final String version;

    private final Boolean isFallBack;
    private final Boolean isProxy;
    private final List<String> templates;

    public GroupLoadedPacket(String name, String wrapper, String version, Boolean isFallBack, Boolean isProxy, List<String> templates) {
        this.name = name;
        this.wrapper = wrapper;
        this.version = version;
        this.isFallBack = isFallBack;
        this.isProxy = isProxy;
        this.templates = templates;
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

    public Boolean isFallBack(){
        return isFallBack;
    }

    public Boolean isProxy(){
        return isProxy;
    }

    public List<String> getTemplates() {
        return templates;
    }
}
