package de.uprank.cloud.packets.type.group;

import java.io.Serializable;

public class GroupDeletePacket implements Serializable {

    private final String groupName;
    private final String wrapperName;

    public GroupDeletePacket(String groupName, String wrapperName) {
        this.groupName = groupName;
        this.wrapperName = wrapperName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getWrapperName() {
        return wrapperName;
    }
}
