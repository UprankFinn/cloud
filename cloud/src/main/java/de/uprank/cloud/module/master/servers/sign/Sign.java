package de.uprank.cloud.module.master.servers.sign;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.servers.Server;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.sign.SignPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Sign {

    private final String serverName;
    private final String group;
    private final String template;

    private final UUID uniqueId;

    private final String world;
    private final int x, y, z;


    private Server server;
    private SignPacket signPacket;

    public Sign(String serverName, String group, String template, UUID uniqueId, String world, int x, int y, int z) {
        this.serverName = serverName;
        this.group = group;
        this.template = template;

        this.uniqueId = uniqueId;

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void sendLines() {
        if (this.signPacket == null) return;

        for (Server servers : MasterModule.getInstance().getServerManager().getServers().values()) {
            if (servers.isFallBack() && servers.getGroup().contains("lobby") && servers.getName().contains("lobby")) {
                MasterModule.getInstance().getServerChannel().writeAndFlush(new Packet(PacketType.GameServerSignPacket.name(), this.signPacket));
            }
        }
    }

    public void updateLines() {
        if (this.server != null) {

            if (this.serverName == null) {
                String status = "§a§lLobby";
                if (this.server.getPlayers() >= this.server.getMaxPlayers()) {
                    status = "§6§lPremium";
                }
                this.signPacket = new SignPacket(this.world, this.x, this.y, this.z, this.server.getName(), new String[]{"[" + this.server.getGroup() + "]", status, server.getMotd(), this.server.getPlayers() + "/" + this.server.getMaxPlayers()}, true);
            }
        } else {
            String[] linesOffline = new String[]{"", "searching", "Server", ""};

            if(server == null) return;

            this.signPacket = new SignPacket(this.world, this.x, this.y, this.z, "no", linesOffline, false);
        }
    }

}
