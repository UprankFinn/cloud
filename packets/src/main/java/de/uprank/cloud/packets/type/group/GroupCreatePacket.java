package de.uprank.cloud.packets.type.group;

import java.io.Serializable;

public class GroupCreatePacket implements Serializable {

    private final String groupName;
    private final String wrapperName;

    private final Boolean isStatic;
    private final Boolean isFallBack;
    private final String version;

    public GroupCreatePacket(String groupName, String wrapperName, Boolean isStatic, Boolean isFallBack, String version) {
        this.groupName = groupName;
        this.wrapperName = wrapperName;

        this.isStatic = isStatic;
        this.isFallBack = isFallBack;

        this.version = version;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getWrapperName() {
        return wrapperName;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public Boolean getFallBack() {
        return isFallBack;
    }

    public String getVersion() {
        return version;
    }
}
