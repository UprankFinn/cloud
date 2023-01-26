package de.uprank.cloud.packets.type.sign;

import java.io.Serializable;

public class SignPacket implements Serializable {

    private final String world;
    private final int x, y, z;

    private final String[] lines;

    private final String serverName;
    private final Boolean isAdded;

    public SignPacket(String world, int x, int y, int z, String serverName, String[] lines, Boolean isAdded) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        this.serverName = serverName;

        this.lines = lines;

        this.isAdded = isAdded;
    }

    public String[] getLines() {
        return lines;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public String getServerName() {
        return serverName;
    }

    public Boolean getAdded() {
        return isAdded;
    }
}
