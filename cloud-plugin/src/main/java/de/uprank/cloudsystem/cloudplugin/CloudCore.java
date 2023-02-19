package de.uprank.cloudsystem.cloudplugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.proxy.ProxyServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloudsystem.cloudapi.CloudAPI;
import de.uprank.cloudsystem.cloudapi.player.PlayerManager;
import de.uprank.cloudsystem.cloudapi.proxy.ProxyServerManager;
import de.uprank.cloudsystem.cloudapi.server.GameServer;
import de.uprank.cloudsystem.cloudapi.server.GameServerManager;
import de.uprank.cloudsystem.cloudplugin.api.gameserver.AbstractGameServerManager;
import de.uprank.cloudsystem.cloudplugin.api.player.AbstractPlayerManager;
import de.uprank.cloudsystem.cloudplugin.api.proxyserver.AbstractProxyServerManager;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import io.netty.channel.Channel;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

@Getter
public class CloudCore extends CloudAPI {

    private static CloudCore plugin;

    private Channel gameServerChannel;
    private Channel proxyServerChannel;

    private String name;
    private String gameId;

    private Boolean isProxy;
    private String template;
    private String servergroup;
    private String wrapper;

    private Integer minMemory;
    private Integer maxMemory;

    private Boolean isFallBack;
    private Boolean isDynamic;

    private String hostName;
    private Integer port;
    private String password;

    private final Map<UUID, String> onlinePlayers;

    private final Jedis jedis;

    private final AbstractPlayerManager abstractPlayerManager;
    private final AbstractGameServerManager abstractGameServerManager;
    private final AbstractProxyServerManager abstractProxyServerManager;

    public CloudCore(String file) {

        plugin = this;

        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(file));

            this.name = jsonObject.get("name").getAsString();
            this.gameId = jsonObject.get("gameId").getAsString();

            this.isProxy = jsonObject.get("isProxy").getAsBoolean();
            this.template = jsonObject.get("template").getAsString();
            this.servergroup = jsonObject.get("group").getAsString();
            this.wrapper = jsonObject.get("wrapper").getAsString();

            this.minMemory = jsonObject.get("minMemory").getAsInt();
            this.maxMemory = jsonObject.get("maxMemory").getAsInt();

            this.isFallBack = jsonObject.get("isFallBack").getAsBoolean();
            this.isDynamic = jsonObject.get("isDynamic").getAsBoolean();

            JsonObject redisJsonObject = jsonObject.getAsJsonObject("redis");

            this.hostName = redisJsonObject.get("hostName").getAsString();
            this.port = redisJsonObject.get("port").getAsInt();
            this.password = redisJsonObject.get("password").getAsString();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.jedis = new Jedis(this.hostName, this.port);
        this.jedis.auth(this.password);
        this.jedis.connect();

        if (this.isProxy) {
            this.setGameServerChannel(CloudProxiedPlugin.getInstance().getServerChannel());
            this.setProxyServerChannel(CloudProxiedPlugin.getInstance().getProxyChannel());
        } else {
            this.setGameServerChannel(CloudBukkitPlugin.getInstance().getServerChannel());
            this.setProxyServerChannel(CloudBukkitPlugin.getInstance().getProxyChannel());
        }

        this.onlinePlayers = new HashMap<>();

        this.abstractPlayerManager = new AbstractPlayerManager();
        this.abstractGameServerManager = new AbstractGameServerManager();
        this.abstractProxyServerManager = new AbstractProxyServerManager();

    }

    public static CloudCore getPlugin() {
        return plugin;
    }

    @Override
    public Map<UUID, String> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    @Override
    public String getCurrentServiceName() {
        return this.name;
    }

    @Override
    public String getGameId() {
        return this.gameId;
    }

    @Override
    public String getCurrentTemplate() {
        return this.template;
    }

    @Override
    public String getCurrentServerGroup() {
        return this.servergroup;
    }

    @Override
    public void startNewService() {
        if (isProxy) {
            CloudProxiedPlugin.getInstance().getProxyChannel().writeAndFlush(new Packet(PacketType.ProxyServerRequestPacket.name(), new ProxyServerRequestPacket(this.servergroup, this.template, this.wrapper, this.minMemory, this.maxMemory, this.isProxy, this.isDynamic)));
        } else {
            CloudBukkitPlugin.getInstance().getServerChannel().writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), new GameServerRequestPacket(this.servergroup, this.template, this.wrapper, this.minMemory, this.maxMemory, false, this.isFallBack, this.isDynamic)));
        }
    }

    @Override
    public Jedis getJedis() {
        return this.jedis;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return this.abstractPlayerManager;
    }

    @Override
    public ProxyServerManager getProxyServerManager() {
        return this.abstractProxyServerManager;
    }

    @Override
    public GameServerManager getGameServerManager() {
        return this.abstractGameServerManager;
    }

    public Channel getGameServerChannel() {
        return gameServerChannel;
    }

    public Channel getProxyServerChannel() {
        return proxyServerChannel;
    }

    public void setGameServerChannel(Channel gameServerChannel) {
        this.gameServerChannel = gameServerChannel;
    }

    public void setProxyServerChannel(Channel proxyServerChannel) {
        this.proxyServerChannel = proxyServerChannel;
    }
}
