package de.uprank.cloud.packets.type.group.template;

import java.io.Serializable;

public class TemplateCreatePacket implements Serializable {

    private final String name;
    private final String group;

    private final Integer minMemory;
    private final Integer maxMemory;

    public TemplateCreatePacket(String name, String group, Integer minMemory, Integer maxMemory) {
        this.name = name;
        this.group = group;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public Integer getMinMemory() {
        return minMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }
}
