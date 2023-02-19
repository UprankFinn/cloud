package de.uprank.cloudsystem.cloudplugin.api.gameserver;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.server.GameServerUpdatePacket;
import de.uprank.cloudsystem.cloudapi.server.GameServer;
import de.uprank.cloudsystem.cloudplugin.CloudCore;

import java.util.HashMap;
import java.util.Map;

public class AbstractGameServer extends GameServer {

    private final String name;
    private final String replayId;

    private Integer playerAmount;
    private Integer maxPlayerAmount;

    private String motd;
    private ServerUtil serverUtil;

    public AbstractGameServer(String name, String replayId, Integer playerAmount, Integer maxPlayerAmount, String motd, ServerUtil serverUtil) {
        this.name = name;
        this.replayId = replayId;

        this.playerAmount = playerAmount;
        this.maxPlayerAmount = maxPlayerAmount;

        this.motd = motd;
        this.serverUtil = serverUtil;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getReplayId() {
        return this.replayId;
    }

    @Override
    public Integer getPlayerAmount() {
        return this.playerAmount;
    }

    @Override
    public Integer getMaxPlayerAmount() {
        return this.maxPlayerAmount;
    }

    @Override
    public String getMotd() {
        return this.motd;
    }

    @Override
    public ServerUtil getServerUtil() {
        return this.serverUtil;
    }

    @Override
    public void setServerUtil(ServerUtil serverUtil) {
        this.serverUtil = serverUtil;

        Map<String, Object> data = new HashMap<>();
        data.put("serverState", serverUtil.name());

        CloudCore.getPlugin().getGameServerChannel().writeAndFlush(new Packet(PacketType.GameServerUpdatePacket.name(), new GameServerUpdatePacket(this.name, data)));
    }
}
