package de.uprank.cloudsystem.cloudapi.server;

import java.util.List;

public interface GameServerManager {

    List<GameServer> getGameServer();
    GameServer getGameServer(String name);

    void requestGameServer(String group, String template);

}
