package de.uprank.cloud.module.master.servers.sign;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.servers.Server;
import de.uprank.cloud.packets.ServerUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class SignManager {

    private final MasterModule masterModule;

    private final List<Sign> signs = new ArrayList<>();

    public SignManager(MasterModule masterModule) {
        this.masterModule = masterModule;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Sign sign : SignManager.this.signs) {
                    if (sign.getServer() == null || MasterModule.getInstance().getServerManager().getServers().containsKey(sign.getServer().getName()) && sign.getServer().getServerState() == ServerUtil.LOBBY.name())
                        continue;
                    sign.getServer().setSign(null);
                    sign.setServer(null);
                }
                for (Server servers : MasterModule.getInstance().getServerManager().getServers().values()) {
                    SignManager.this.checkServerSign(servers);
                }
                for (Sign sign : SignManager.this.signs) {
                    sign.updateLines();
                    sign.sendLines();
                }
            }
        }, 500, 500);
    }

    public void checkServerSign(Server server) {
        if (server.getSign() != null) {
            if (server.getServerState() != ServerUtil.LOBBY.name() && server.getSign().getServerName() == null) {
                Sign sign = server.getSign();
                sign.setServer(null);
                server.setSign(null);
                return;
            }
            server.getSign().updateLines();
        }
        if (server.getGroup() != null && server.getTemplate() != null && server.getSign() == null && server.getServerState() == ServerUtil.LOBBY.name()) {
            for (Sign sign : this.signs) {
                if (sign.getServerName() != null && sign.getGroup() == server.getGroup() && sign.getTemplate() == server.getTemplate() && sign.getServer() != null)
                    continue;
                sign.setServer(server);
                server.setSign(sign);
                return;
            }
            return;
        } else {
            if (server.getSign() != null) {
                for (Sign sign : this.signs) {
                    if (sign.getServerName() == null || !sign.getServerName().equalsIgnoreCase(server.getName())) {
                        server.setSign(sign);
                        sign.setServer(server);
                    }
                }
            }
        }
    }

    public void loadSigns() {
        this.signs.clear();

        for (Server server : this.masterModule.getServerManager().getServers().values()) {
            server.setSign(null);
        }

        this.masterModule.getCloudSignManager().getCloudSigns().forEach((uuid, cloudSign) -> {
            this.masterModule.info("[CloudSigns] load Sign " + cloudSign.getUniqueId().toString());
            Sign sign = new Sign("null", cloudSign.getGroup(),cloudSign.getTemplate(), cloudSign.getUniqueId(), cloudSign.getWorld(),cloudSign.getX(),cloudSign.getY(),cloudSign.getZ());
            sign.updateLines();
            this.signs.add(sign);
        });

        this.signs.forEach(sign -> this.masterModule.info("[CloudSigns] loaded Sign " + sign.getUniqueId()));

    }

}
