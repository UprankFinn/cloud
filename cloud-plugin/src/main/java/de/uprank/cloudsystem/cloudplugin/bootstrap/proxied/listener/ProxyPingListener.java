package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.listener;

import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Random;

public class ProxyPingListener implements Listener {

    private final CloudProxiedPlugin plugin;

    public ProxyPingListener(CloudProxiedPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler
    public void onProxyPingEvent(ProxyPingEvent event) {

        ServerPing serverPing = event.getResponse();

        if (this.plugin.getProxyConfig().isMaintenance()) {

            serverPing.setDescriptionComponent(
                    new TextComponent(
                            this.plugin.getProxyConfig().getMaintenanceLine_1() + "\n" +
                                    this.plugin.getProxyConfig().getMaintenance_Lines_2().get(new Random().nextInt(this.plugin.getProxyConfig().getMaintenance_Lines_2().size()))));

            serverPing.getVersion().setName(":D");
            serverPing.getPlayers().setOnline(0);
            serverPing.getPlayers().setMax(0);

        } else {
            serverPing.setDescriptionComponent(new TextComponent(this.plugin.getProxyConfig().getLine_1().replace("&", "§") +
                    "\n" + this.plugin.getProxyConfig().getLine_2().get(new Random().nextInt(this.plugin.getProxyConfig().getLine_2().size())).replace("&", "§")));

            serverPing.getPlayers().setOnline(this.plugin.getCloudCore().getOnlinePlayerCount());
            serverPing.getPlayers().setMax(this.plugin.getProxyConfig().getSlots());

        }

    }

}
