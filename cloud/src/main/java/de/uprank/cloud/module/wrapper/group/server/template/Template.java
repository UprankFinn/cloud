package de.uprank.cloud.module.wrapper.group.server.template;

public class Template {

    private final String name;
    private final Integer minMemory;
    private final Integer maxMemory;
    private final Integer minOnline;
    private final Integer maxOnline;

    public Template(String name, Integer minMemory, Integer maxMemory, Integer minOnline, Integer maxOnline) {
        this.name = name;
        this.minMemory = minMemory;
        this.maxMemory = maxMemory;
        this.minOnline = minOnline;
        this.maxOnline = maxOnline;
    }

    public String getName() {
        return name;
    }

    public Integer getMinMemory() {
        return minMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public Integer getMinOnline() {
        return minOnline;
    }

    public Integer getMaxOnline() {
        return maxOnline;
    }
}
