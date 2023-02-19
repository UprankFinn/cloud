package de.uprank.cloud.module.master.database.signs;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CloudSign {

    private final UUID uniqueId;

    private final String group;
    private final String template;

    private final String world;
    private final int x,y,z;
    private final float yaw, pitch;

    public CloudSign(String group, String template, String world, int x, int y, int z, float yaw, float pitch) {
        this.uniqueId = UUID.randomUUID();

        this.group = group;
        this.template = template;

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
