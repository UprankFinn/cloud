package de.uprank.cloud.packets.type.wrapper;

import java.io.Serializable;

public class WrapperStopServerPacket implements Serializable {

    private final String wrapper;

    public WrapperStopServerPacket(String wrapper) {
        this.wrapper = wrapper;
    }

    public String getWrapper() {
        return wrapper;
    }
}
