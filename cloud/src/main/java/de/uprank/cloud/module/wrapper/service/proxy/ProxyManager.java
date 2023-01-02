package de.uprank.cloud.module.wrapper.service.proxy;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.service.server.ServerManager;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyManager {

    private final WrapperModule wrapperModule;

    private final List<Proxy> proxies;
    private final Map<String, Long> nameReservation;

    public ProxyManager(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;

        this.proxies = new ArrayList<>();
        this.nameReservation = new HashMap<>();
    }

    @SneakyThrows
    public void startService(String group, String template, Integer minMemory, Integer maxMemory, Integer amount, Boolean isDynamic) {

        /*String serverName = findServerName(group, template);

        File file = new File("temporary/proxies/" + group.toLowerCase(Locale.ROOT) + "/" + template.toLowerCase(Locale.ROOT) + "/" + serverName.toLowerCase(Locale.ROOT) + "/");
        if (!(file.exists())) file.mkdir();
        FileUtils.copyFileToDirectory(new File("default/plugins/"), new File(file + "plugins/"));
        FileUtils.copyFileToDirectory(new File("default/waterfall.jar"), new File(file.toURI()));
        FileUtils.copyFileToDirectory(new File("templates/proxies/" + group + "/" + template + "/"), file);

        Server server = new Server(serverName, InetAddress.getLocalHost().getHostName(), this.getPort(), minMemory, maxMemory, this.wrapperModule.getName(), group, template, ServerUtil.START_UP, false, isDynamic);
        server.start();*/

    }

    public String findProxyName(String group, String template) {
        int current = 1;
        while (true) {
            String name = group.toLowerCase(Locale.ROOT) + "-" + template.toLowerCase(Locale.ROOT) + "-" + current;
            if (getProxyByName(name) != null || nameReservation.containsKey(name.toLowerCase())) {
                current++;
            } else {
                return name;
            }
        }

    }

    public Proxy getProxyByName(String name) {
        for (Proxy proxies : this.proxies) {
            //if (proxies.getName().contains(name)) return proxies;
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
