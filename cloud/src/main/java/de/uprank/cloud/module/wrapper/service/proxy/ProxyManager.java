package de.uprank.cloud.module.wrapper.service.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.service.server.Server;
import de.uprank.cloud.module.wrapper.service.server.ServerManager;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.ServerUtil;
import de.uprank.cloud.packets.type.proxy.ProxyServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.module.Configuration;
import java.net.ServerSocket;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class ProxyManager {

    private final WrapperModule wrapperModule;

    private final Map<String, Proxy> proxies;
    private final Map<String, Process> processes;

    public ProxyManager(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;

        this.proxies = new HashMap<>();
        this.processes = new HashMap<>();
    }

    @SneakyThrows
    public void startService(String group, String template, String gameId, Integer minMemory, Integer maxMemory, Boolean fallBack, Integer amount, Boolean isDynamic) {

        String serverName = findProxyName(group, template);

        /*File file = new File("temporary/proxies/" + group.toLowerCase(Locale.ROOT) + "/" + template.toLowerCase(Locale.ROOT) + "/" + serverName.toLowerCase(Locale.ROOT) + "/");
        if (!(file.exists())) file.mkdir();
        FileUtils.copyFileToDirectory(new File("default/proxied/plugins/"), new File(file + "plugins/"));
        FileUtils.copyFileToDirectory(new File("default/proxied/waterfall.jar"), new File(file.toURI()));
        FileUtils.copyFileToDirectory(new File("templates/proxies/" + group + "/" + template + "/"), file);*/

        File file = new File("temporary/proxies/" + group + "/" + serverName + "/");
        if (!(file.exists())) file.mkdir();
        FileUtils.copyDirectory(new File("default/proxied/"), file);
        FileUtils.copyDirectory(new File("templates/proxies/" + group + "/" + template + "/"), file);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", serverName);
        jsonObject.addProperty("gameId", gameId);

        jsonObject.addProperty("isProxy", true);
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

        try (FileWriter fileWriter = new FileWriter("temporary/proxies/" + group.toLowerCase(Locale.ROOT) + "/" + serverName.toLowerCase(Locale.ROOT) + "/service.json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Proxy proxy = new Proxy(serverName, "127.0.0.1", findPort(), minMemory, maxMemory, this.wrapperModule.getName(), group, template, ServerUtil.START_UP, true);
        this.proxies.put(serverName, proxy);
        proxy.start();

        this.wrapperModule.getProxyChannel().writeAndFlush(new Packet(PacketType.ProxyServerStartPacket.name(), new ProxyServerStartPacket(proxy.getName(), proxy.getHostName(), proxy.getPort(), proxy.getWrapper(), proxy.getGroup(), proxy.getTemplate(), minMemory, maxMemory, proxy.getServerUtil(), false, proxy.isDynamic())));
        this.wrapperModule.info("&bstarting new proxy on " + proxy.getHostName() + ":" + proxy.getPort() + "&8(&b" + proxy.getName() + "&8)");


    }
    public String findProxyName(String group, String template) {
        int current = 1;
        while (getProxyByName(group + "-" + template + "-" + current) != null) {
            current++;
        }
        return group + "-" + template + "-" + current;
    }

    public Proxy getProxyByName(String name) {
        if (this.proxies.containsKey(name)) {
            return this.proxies.get(name);
        }
        return null;
    }

    @SneakyThrows
    public Integer findPort() {
        int current = 25565;
        ServerSocket serverSocket = new ServerSocket(0);
        while (serverSocket.getLocalPort() == current) {
            current++;
        }
        return current;
    }

    public Map<String, Proxy> getProxies() {
        return proxies;
    }
}
