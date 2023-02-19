package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.proxy.server.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.proxy.server.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloud.packets.type.server.GameServerUpdatePacket;
import de.uprank.cloud.packets.util.StopReason;
import de.uprank.cloudsystem.cloudplugin.CloudCore;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.commands.CloudServerCommand;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.database.signs.CloudSignManager;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.listener.PlayerJoinListener;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.listener.PlayerQuitListener;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.player.PlayerHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.proxy.ProxyHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.server.ServerHandlerClient;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.netty.wrapper.WrapperHandlerClient;
import io.netty.channel.Channel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class CloudBukkitPlugin extends JavaPlugin {

    @Getter
    private static CloudBukkitPlugin instance;

    private CloudCore cloudCore;

    private MongoClient mongoClient;

    private CloudSignManager cloudSignManager;

    private Map<Location, String> signs;

    private Channel playerChannel;
    private Channel proxyChannel;
    private Channel serverChannel;
    private Channel wrapperChannel;

    private PlayerHandlerClient playerHandlerClient;
    private ProxyHandlerClient proxyHandlerClient;
    private ServerHandlerClient serverHandlerClient;
    private WrapperHandlerClient wrapperHandlerClient;

    public CloudBukkitPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.cloudCore = new CloudCore("service.json");

        if (this.getCloudCore().getName() != null && this.getCloudCore().getHostName() != null && this.getCloudCore().getPort() != null) {
            this.playerHandlerClient = new PlayerHandlerClient(this, "127.0.0.1", 2300);
            this.proxyHandlerClient = new ProxyHandlerClient(this, "127.0.0.1", 2301);
            this.serverHandlerClient = new ServerHandlerClient(this, "127.0.0.1", 2302);
            this.wrapperHandlerClient = new WrapperHandlerClient(this, "127.0.0.1", 2303);

            new Thread(this.playerHandlerClient).start();
            new Thread(this.proxyHandlerClient).start();
            new Thread(this.serverHandlerClient).start();
            new Thread(this.wrapperHandlerClient).start();

        }

        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://root:mVEXrxgsqyKFhsdfiEuApmmkht3MjPuz@45.142.115.211:27017/admin"));

        this.cloudSignManager = new CloudSignManager(this);
        this.signs = new HashMap<>();

        new CloudServerCommand(this);

        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        Map<String, Object> data = new HashMap<>();
        data.put("maxPlayers", Bukkit.getMaxPlayers());
        data.put("motd", Bukkit.getMotd());
        data.put("serverState", ServerUtil.LOBBY.name());
        data.put("group", this.cloudCore.getCurrentServerGroup());
        data.put("template", this.cloudCore.getTemplate());
        data.put("wrapper", this.cloudCore.getWrapper());
        this.serverChannel.writeAndFlush(new Packet(PacketType.GameServerUpdatePacket.name(), new GameServerUpdatePacket(this.cloudCore.getName(), data)));
        this.proxyChannel.writeAndFlush(new Packet(PacketType.ProxyAddGameServerPacket.name(), new ProxyAddGameServerPacket(this.cloudCore.getCurrentServiceName(), Bukkit.getServer().getIp(), Bukkit.getServer().getPort())));

    }

    @Override
    public void onDisable() {
        this.serverChannel.writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), new GameServerRequestPacket(this.cloudCore.getServergroup(),
                this.cloudCore.getTemplate(), this.cloudCore.getWrapper(), this.cloudCore.getMinMemory(), this.cloudCore.getMaxMemory(),
                false, this.cloudCore.getIsFallBack(), this.cloudCore.getIsDynamic())));

        this.proxyChannel.writeAndFlush(new Packet(PacketType.ProxyRemoveGameServerPacket.name(), new ProxyRemoveGameServerPacket(this.cloudCore.getName())));

        this.serverChannel.writeAndFlush(new Packet(PacketType.GameServerStopPacket.name(), new GameServerStopPacket(
                this.cloudCore.getName(),StopReason.Normal_STOP,this.cloudCore.getServergroup(),this.cloudCore.getTemplate(),this.cloudCore.getWrapper(),
                this.cloudCore.getHostName(),this.cloudCore.getPort(),this.cloudCore.getMinMemory(),this.cloudCore.getMaxMemory(),
                false,
                this.cloudCore.getIsFallBack(),this.cloudCore.getIsDynamic())));

        if (this.wrapperChannel.isOpen() && this.wrapperChannel.isActive() && this.wrapperChannel.isRegistered()) {
            this.wrapperChannel.close();
        }
    }

    public Channel getPlayerChannel() {
        return playerChannel;
    }

    public void setPlayerChannel(Channel playerChannel) {
        this.playerChannel = playerChannel;
    }

    public Channel getProxyChannel() {
        return proxyChannel;
    }

    public void setProxyChannel(Channel proxyChannel) {
        this.proxyChannel = proxyChannel;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public Channel getWrapperChannel() {
        return wrapperChannel;
    }

    public void setWrapperChannel(Channel wrapperChannel) {
        this.wrapperChannel = wrapperChannel;
    }
}
