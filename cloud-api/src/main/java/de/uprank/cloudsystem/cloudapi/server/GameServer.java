package de.uprank.cloudsystem.cloudapi.server;

import de.uprank.cloud.packets.ServerUtil;

import java.util.List;
import java.util.UUID;

public abstract class GameServer {

    public abstract String getName();
    public abstract String getReplayId();

    public abstract Integer getPlayerAmount();
    public abstract void setPlayerAmount(Integer playerAmount);

    public abstract Integer getMaxPlayerAmount();
    public abstract void setMaxPlayerAmount(Integer maxPlayerAmount);

    public abstract String getMotd();
    public abstract void setMotd(String motd);

    public abstract ServerUtil getServerUtil();
    public abstract void setServerUtil(ServerUtil serverUtil);

}
