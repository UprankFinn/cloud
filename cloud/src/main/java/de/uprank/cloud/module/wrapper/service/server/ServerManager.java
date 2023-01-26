package de.uprank.cloud.module.wrapper.service.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class ServerManager {

    private final WrapperModule wrapperModule;

    private final Map<String, Server> servers;
    private final Map<String, Process> processes;

    public ServerManager(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;

        this.servers = new HashMap<>();
        this.processes = new HashMap<>();
    }

    @SneakyThrows
    public void startService(String group, String template, String gameId, Integer minMemory, Integer maxMemory, Boolean fallBack, Boolean isDynamic, Integer amount) {

        String serverName = findServerName(group, template);

        File file = new File("temporary/servers/" + group + "/" + serverName + "/");
        if (!(file.exists())) file.mkdir();
        FileUtils.copyDirectory(new File("default/bukkit/"), file);
        FileUtils.copyDirectory(new File("templates/servers/" + group + "/" + template + "/"), file);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", serverName);
        jsonObject.addProperty("gameId", gameId);

        jsonObject.addProperty("isProxy", false);
        jsonObject.addProperty("template", template);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("wrapper", this.wrapperModule.getName());

        jsonObject.addProperty("minMemory", minMemory);
        jsonObject.addProperty("maxMemory", maxMemory);

        jsonObject.addProperty("isFallBack", fallBack);
        jsonObject.addProperty("isDynamic", isDynamic);

        JsonObject insert = new JsonObject();
        insert.addProperty("hostName", "45.142.115.211");
        insert.addProperty("port", 6379);
        insert.addProperty("password", "ReFu26m9qjs2R9FPyhU8goJX6uP45D8n");

        jsonObject.add("redis", insert);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter("temporary/servers/" + group + "/" + serverName + "/service.json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Server server = new Server(serverName, gameId, "127.0.0.1", getPort(), minMemory, maxMemory, this.wrapperModule.getName(), group, template, ServerUtil.START_UP, fallBack, true);
        this.servers.put(serverName, server);
        server.start();

        this.wrapperModule.getChannel().writeAndFlush(new Packet(PacketType.GameServerStartPacket.name(), new GameServerStartPacket(server.getName(), server.getGameId(), server.getHostName(), server.getPort(), server.getWrapper(), server.getGroup(), server.getTemplate(), minMemory, maxMemory, server.getServerUtil(), false, server.isFallBack(), server.isDynamic())));
        this.wrapperModule.info("&bstarting new service on " + server.getHostName() + ":" + server.getPort() + "&8(&b" + server.getName() + "&8)");

    }

    public void stopService(String name) {

        if (getServer(name) != null) {
            WrapperModule.getInstance().info("&cstopping Service with name " + name);
            getServer(name).shutdown();
        }

    }

    public String findServerName(String group, String template) {
        int current = 1;
        while (getServer(group + "-" + template + "-" + current) != null) {
            current++;
        }
        return group + "-" + template + "-" + current;
    }

    public Server getServer(String server) {
        if (this.servers.containsKey(server)) {
            return this.servers.get(server);
        }
        return null;
    }

    private Integer getPort() {
        try {
            ServerSocket socket = new ServerSocket(0);
            Throwable var2 = null;

            Integer var3;
            try {
                socket.setReuseAddress(true);
                var3 = socket.getLocalPort();
            } catch (Throwable var13) {
                var2 = var13;
                throw var13;
            } finally {
                if (socket != null) {
                    if (var2 != null) {
                        try {
                            socket.close();
                        } catch (Throwable var12) {
                            var2.addSuppressed(var12);
                        }
                    } else {
                        socket.close();
                    }
                }

            }

            return var3;
        } catch (IOException var15) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, (String) null, var15);
            return 0;
        }
    }

}
