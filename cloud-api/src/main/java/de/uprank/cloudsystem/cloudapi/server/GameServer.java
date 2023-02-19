package de.uprank.cloudsystem.cloudapi.server;

import de.uprank.cloud.packets.ServerUtil;

public abstract class GameServer {

    public abstract String getName();
    public abstract String getReplayId();

    public abstract Integer getPlayerAmount();
    public abstract Integer getMaxPlayerAmount();

    public abstract String getMotd();
    public abstract ServerUtil getServerUtil();
    public abstract void setServerUtil(ServerUtil serverUtil);

}
