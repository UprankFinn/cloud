package de.uprank.cloudsystem.cloudapi.player;

import de.uprank.cloudsystem.cloudapi.proxy.ProxyServer;
import de.uprank.cloudsystem.cloudapi.server.GameServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public abstract class Player {

    public abstract UUID getUniqueId();

    public abstract String getName();
    public abstract void setName(String name);

    public abstract ProxyServer getProxy();
    public abstract GameServer getServer();

    public abstract void sendMessage(TextComponent textComponent);

}
