package de.uprank.cloudsystem.cloudplugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloudsystem.cloudapi.CloudAPI;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import lombok.Getter;
import redis.clients.jedis.Jedis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.UUID;

@Getter
public class CloudCore extends CloudAPI {

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

    private final Jedis jedis;

    public CloudCore(String file) {

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

    }

    @Override
    public Integer getOnlinePlayerCount() {
        return Integer.valueOf((int) this.jedis.llen("players"));
    }

    @Override
    public List<UUID> getOnlineUniqueIds() {
        return null;
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
            CloudProxiedPlugin.getInstance().getChannel().writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), new GameServerRequestPacket(this.servergroup, this.template, this.wrapper, this.minMemory, this.maxMemory, false, this.isFallBack, this.isDynamic)));
        } else {
            CloudBukkitPlugin.getInstance().getChannel().writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), new GameServerRequestPacket(this.servergroup, this.template, this.wrapper, this.minMemory, this.maxMemory, false, this.isFallBack, this.isDynamic)));
        }
    }

    @Override
    public Jedis getJedis() {
        return this.jedis;
    }
}
