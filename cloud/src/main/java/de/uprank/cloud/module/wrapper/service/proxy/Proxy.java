package de.uprank.cloud.module.wrapper.service.proxy;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.proxy.ProxyServerStopPacket;
import de.uprank.cloud.packets.util.StopReason;
import io.netty.channel.Channel;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Proxy {

    private final String name;

    private final String hostName;
    private final Integer port;

    private final Integer minMemory;
    private final Integer maxMemory;

    private final String wrapper;

    private final String group;
    private final String template;

    private ServerUtil serverUtil;

    private final Boolean isDynamic;

    private final List<UUID> players = new ArrayList<>();

    private long timeOut = -1;
    private long startupTimeout = -1;
    private long stateTimeOut = -1;
    private Channel channel;

    private Process process;
    private Integer processId;

    public Proxy(String name, String hostName, Integer port, Integer minMemory, Integer maxMemory, String wrapper, String group, String template, ServerUtil serverUtil, Boolean isDynamic) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;

        this.minMemory = minMemory;
        this.maxMemory = maxMemory;

        this.wrapper = wrapper;

        this.group = group;
        this.template = template;

        this.serverUtil = serverUtil;

        this.isDynamic = isDynamic;
    }

    public void start() {

        new Thread(() -> {
            File file = new File("temporary/proxies/" + this.group + "/" + this.name + "/");

            if (!(file.exists())) file.mkdirs();

            this.changeFile(this.name.toLowerCase(Locale.ROOT), this.port);

            try {

                ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
                processBuilder.directory(file);
                processBuilder.command("java", "-Xms" + this.minMemory + "M", "-Xms" + this.maxMemory + "M", "-jar", "BungeeCord.jar");
                this.process = processBuilder.start();
                this.processId = Integer.valueOf((int) this.process.pid());

                WrapperModule.getInstance().getProxyManager().getProcesses().put(this.name, process);

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }).start();

    }

    public void shutdown() {
        new Thread(() -> {
            try {
                WrapperModule.getInstance().info("&cstopping Proxy with name " + this.name);

                WrapperModule.getInstance().getProxyChannel().writeAndFlush(new Packet(PacketType.ProxyServerStopPacket.name(), new ProxyServerStopPacket(this.name, StopReason.Normal_STOP, this.hostName, this.port, group, template, this.wrapper, minMemory, maxMemory, isDynamic)));

                WrapperModule.getInstance().getProxyManager().getProcesses().get(this.name).destroyForcibly();
                WrapperModule.getInstance().getProxyManager().getProcesses().remove(this.name);
                Process killserverProcess = Runtime.getRuntime().exec("kill -9" + this.processId);
                killserverProcess.waitFor();

                Files.delete(Paths.get("temporary/proxies/" + this.group + "/" + this.name + "/"));

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
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

    @SneakyThrows
    public void changeFile(String serverName, Integer port) {

        try {
            List<String> lines = new ArrayList<>();
            String lineToEdit = "    host: 0.0.0.0:25565";
            String newLine = "    host: 0.0.0.0:" + port;
            BufferedReader bufferedReader = new BufferedReader(new FileReader("temporary/proxies/" + this.group + "/" + this.name + "/config.yml"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(lineToEdit)) {
                    lines.add(newLine);
                } else {
                    lines.add(line);
                }
            }
            bufferedReader.close();
            FileWriter fileWriter = new FileWriter("temporary/proxies/" + this.group + "/" + this.name + "/config.yml");
            for (String newline : lines) {
                fileWriter.write(newline);
                fileWriter.write("\n");
            }
            fileWriter.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

}
