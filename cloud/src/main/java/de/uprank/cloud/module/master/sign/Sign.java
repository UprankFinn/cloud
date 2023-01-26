package de.uprank.cloud.module.master.sign;

import de.uprank.cloud.Master;
import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.servers.Server;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.sign.SignPacket;
import lombok.Getter;

import java.util.Iterator;

@Getter
public class Sign {

    private String world;
    private Integer x, y, z;

    private String group;
    private String template;

    private Server server;

    private SignPacket signPacket;

    public Sign(String world, Integer x, Integer y, Integer z, String group, String template) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.group = group;
        this.template = template;
    }

    public void sendLines() {
        Packet packet = new Packet(PacketType.GameServerSignPacket.name(), signPacket);
        Iterator<Server> serverIterator = MasterModule.getInstance().getServerManager().getServers().values().iterator();
        while (serverIterator.hasNext()) {
            Server servers = serverIterator.next();
            if (servers.isFallBack()) {
                servers.sendPacket(new Packet(PacketType.GameServerSignPacket.name(), signPacket));
            }
        }
        for (Server servers : MasterModule.getInstance().getServerManager().getServers().values()) {
            if (servers.isFallBack()) {
                servers.sendPacket(new Packet(PacketType.GameServerSignPacket.name(), signPacket));
            }
        }
    }

    public void updateLines() {
        if (server != null) {



        }
    }

}
