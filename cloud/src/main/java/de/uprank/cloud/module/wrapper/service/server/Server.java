package de.uprank.cloud.module.wrapper.service.server;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import io.netty.channel.Channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class Server {

    private final String name;
    private final String gameId;

    private final String hostName;
    private final Integer port;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final String wrapper;

    private final String group;
    private final String template;

    private ServerUtil serverUtil;

    private final Boolean isFallBack;
    private final Boolean isDynamic;

    private final List<UUID> players = new ArrayList<>();

    private long timeOut = -1;
    private long startupTimeout = -1;
    private long stateTimeOut = -1;
    private Channel channel;

    private Process process;
    private Integer processId;

    public Server(String name, String gameId, String hostName, Integer port, Integer minMemory, Integer maxMemory, String wrapper, String group, String template, ServerUtil serverUtil, Boolean isFallBack, Boolean isDynamic) {
        this.name = name;
        this.gameId = gameId;

        this.hostName = hostName;
        this.port = port;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.wrapper = wrapper;

        this.group = group;
        this.template = template;

        this.serverUtil = serverUtil;

        this.isFallBack = isFallBack;
        this.isDynamic = isDynamic;
    }

    public void start() {

        Executors.newSingleThreadExecutor().execute(() -> {

            File file = new File("temporary/servers/" + this.group + "/" + this.name + "/");

            try {

                ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
                processBuilder.directory(file);
                processBuilder.command("java", "-Xms" + this.minMemory + "M", "-Xms" + this.maxMemory + "M", "-jar", "paper.jar", "--online-mode", "false", "--host", this.hostName, "--port", this.port.toString());
                this.process = processBuilder.start();

                this.processId = Integer.valueOf((int) this.process.pid());

                WrapperModule.getInstance().getChannel().writeAndFlush(new Packet(PacketType.GameServerStartPacket.name(), new GameServerStartPacket(this.name, this.gameId, this.hostName, this.port, this.wrapper, this.group, this.template, minMemory, maxMemory, this.serverUtil, false, this.isFallBack, this.isDynamic)));
                WrapperModule.getInstance().info("&bstarting new service on " + this.hostName + ":" + this.port + "&8(&b" + this.name + "&8)");

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });

    }

    public void shutdown() {
        this.process.destroy();
        try {
            Runtime.getRuntime().exec("kill " + this.processId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WrapperModule.getInstance().getChannel().writeAndFlush(new Packet(PacketType.GameServerStopPacket.name(), new GameServerStopPacket(name, hostName, port, wrapper)));
        WrapperModule.getInstance().info("&cstopping Service with name " + this.name);

        if (!(this.process.isAlive())) {
            File file = new File("temporary/servers/" + this.group + "/" + this.name + "/");
            if (file.exists()) {
                file.delete();
            }
        }

    }

    public void sendPacket(Packet packet) {
        new Thread(() -> {
            if (this.channel != null) {
                this.channel.writeAndFlush(packet);
            }
        }).start();
    }

    public String getName() {
        return name;
    }

    public String getGameId() {
        return gameId;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getPort() {
        return port;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getGroup() {
        return group;
    }

    public ServerUtil getServerUtil() {
        return serverUtil;
    }

    public void setServerUtil(ServerUtil serverUtil) {
        this.serverUtil = serverUtil;
    }

    public String getTemplate() {
        return template;
    }

    public Boolean isFallBack() {
        return isFallBack;
    }

    public Boolean isDynamic() {
        return isDynamic;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public long getStartupTimeout() {
        return startupTimeout;
    }

    public void setStartupTimeout(long startupTimeout) {
        this.startupTimeout = startupTimeout;
    }

    public long getStateTimeOut() {
        return stateTimeOut;
    }

    public void setStateTimeOut(long stateTimeOut) {
        this.stateTimeOut = stateTimeOut;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
