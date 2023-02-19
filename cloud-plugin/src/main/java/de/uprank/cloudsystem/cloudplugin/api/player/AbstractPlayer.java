package de.uprank.cloudsystem.cloudplugin.api.player;

import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.player.PlayerMessagePacket;
import de.uprank.cloudsystem.cloudapi.player.Player;
import de.uprank.cloudsystem.cloudapi.proxy.ProxyServer;
import de.uprank.cloudsystem.cloudapi.server.GameServer;
import de.uprank.cloudsystem.cloudplugin.api.gameserver.AbstractGameServer;
import de.uprank.cloudsystem.cloudplugin.api.proxyserver.AbstractProxyServer;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.w3c.dom.Text;

import java.util.UUID;

public class AbstractPlayer extends Player {

    private final UUID uniqueId;
    private String name;

    private AbstractProxyServer proxyServer;
    private AbstractGameServer gameServer;

    public AbstractPlayer(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ProxyServer getProxy() {
        return this.proxyServer;
    }

    @Override
    public GameServer getServer() {
        return this.gameServer;
    }

    @Override
    public void sendMessage(TextComponent textComponent) {
        CloudProxiedPlugin.getInstance().getProxyChannel().writeAndFlush(new Packet(PacketType.PlayerMessagePacket.name(), new PlayerMessagePacket(this.uniqueId, textComponent.getText())));
    }
}
