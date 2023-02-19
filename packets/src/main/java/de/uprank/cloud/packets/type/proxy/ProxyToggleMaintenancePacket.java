package de.uprank.cloud.packets.type.proxy;

import java.io.Serializable;

public class ProxyToggleMaintenancePacket implements Serializable {

    private final Boolean maintenance;

    public ProxyToggleMaintenancePacket(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    public Boolean getMaintenance() {
        return maintenance;
    }
}
