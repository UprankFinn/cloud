package de.uprank.cloud.packets.type.sign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SignServerUpdatePacket implements Serializable {

    private final String group;
    private final String template;

    private final List<String[]> servers = new ArrayList<>();

    public SignServerUpdatePacket(String group, String template) {
        this.group = group;
        this.template = template;
    }

    public String getGroup() {
        return group;
    }

    public String getTemplate() {
        return template;
    }

    public List<String[]> getServers() {
        return servers;
    }
}
