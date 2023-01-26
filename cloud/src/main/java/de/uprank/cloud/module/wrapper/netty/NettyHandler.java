package de.uprank.cloud.module.wrapper.netty;

import de.uprank.cloud.module.wrapper.WrapperModule;
import de.uprank.cloud.module.wrapper.group.proxy.ProxyGroup;
import de.uprank.cloud.module.wrapper.group.server.ServiceGroup;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.group.GroupCreatePacket;
import de.uprank.cloud.packets.type.group.GroupLoadedPacket;
import de.uprank.cloud.packets.type.group.template.TemplateCreatePacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStopPacket;
import de.uprank.cloud.packets.type.wrapper.WrapperStartPacket;
import de.uprank.cloud.packets.type.wrapper.WrapperStopPacket;
import de.uprank.cloud.util.VersionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class NettyHandler extends SimpleChannelInboundHandler<Object> {

    private final WrapperModule wrapperModule;

    public NettyHandler(WrapperModule wrapperModule) {
        this.wrapperModule = wrapperModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

        WrapperStartPacket wrapperStartPacket = new WrapperStartPacket(this.wrapperModule.getName(), this.wrapperModule.getHostName(), this.wrapperModule.getMaxMemory(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.WrapperStartPacket.name(), wrapperStartPacket));
        this.wrapperModule.setChannel(channelHandlerContext.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.wrapperModule.getServerManager().getServers().forEach((name, servers) -> servers.shutdown());
        this.wrapperModule.getProxyManager().getProxies().forEach((name, proxy) -> proxy.shutdown());
        channelHandlerContext.writeAndFlush(new Packet(PacketType.WrapperStopPacket.name(), new WrapperStopPacket(this.wrapperModule.getName(), this.wrapperModule.getHostName())));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {

        new Thread(() -> {

            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.GameServerStopPacket.name())) {
                GameServerStopPacket gameServerStopPacket = (GameServerStopPacket) packet.getObject();
                this.wrapperModule.info(gameServerStopPacket.getName() + ":" + gameServerStopPacket.getHostName() + ":" + gameServerStopPacket.getPort() + ":" + gameServerStopPacket.getWrapper());

                if (gameServerStopPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getServerManager().getServer(gameServerStopPacket.getName()).shutdown();
                    this.wrapperModule.info("&cstopping Service with name " + gameServerStopPacket.getName());
                    this.wrapperModule.getServerManager().getServers().remove(gameServerStopPacket.getName());

                    this.wrapperModule.getServerManager().getServers().forEach(((s, server) -> {
                        this.wrapperModule.info("&cactive servers : " + server.getName());
                    }));

                }

            } else if (packet.getKey().equals(PacketType.GameServerRequestPacket.name())) {
                GameServerRequestPacket gameServerRequestPacket = (GameServerRequestPacket) packet.getObject();

                if (gameServerRequestPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getServerManager().startService(gameServerRequestPacket.getGroup(), gameServerRequestPacket.getTemplate(), String.valueOf(UUID.randomUUID().toString().split("-")[0]), gameServerRequestPacket.getMinMemory(), gameServerRequestPacket.getMaxMemory(), gameServerRequestPacket.isFallBack(), gameServerRequestPacket.isDynamic(), 1);
                }

            } else if (packet.getKey().equals(PacketType.ProxyRequestPacket.name())) {
                ProxyServerRequestPacket proxyServerRequestPacket = (ProxyServerRequestPacket) packet.getObject();

                if (proxyServerRequestPacket.getWrapper().equals(this.wrapperModule.getName())) {
                    this.wrapperModule.getProxyManager().startService(proxyServerRequestPacket.getGroup(), proxyServerRequestPacket.getTemplate(), String.valueOf(UUID.randomUUID().toString().split("-")[0]), proxyServerRequestPacket.getMinMemory(), proxyServerRequestPacket.getMaxMemory(), false,1, proxyServerRequestPacket.isDynamic());
                }

            } else if (packet.getKey().equals(PacketType.GROUP_LOADED_PACKET.name())) {
                GroupLoadedPacket groupLoadedPacket = (GroupLoadedPacket) packet.getObject();

                if (!(groupLoadedPacket.isProxy())) {

                    if (!(new File("templates/servers/" + groupLoadedPacket.getName() + "/").exists())) {
                        new File("templates/servers/" + groupLoadedPacket.getName() + "/").mkdirs();
                    }

                    groupLoadedPacket.getTemplates().forEach((templates) -> {
                        if (!(new File("templates/servers/" + groupLoadedPacket.getName() + "/" + templates + "/" + "plugins/").exists())) {
                            new File("templates/servers/" + groupLoadedPacket.getName() + "/" + templates + "/" + "plugins/").mkdirs();
                        }
                    });

                    this.wrapperModule.getServiceGroupManager().getServiceGroups().put(groupLoadedPacket.getName(), new ServiceGroup(groupLoadedPacket.getName(), groupLoadedPacket.getWrapper(), groupLoadedPacket.getVersion(), groupLoadedPacket.isFallBack()));
                    this.wrapperModule.info("&bcreating new files for group " + groupLoadedPacket.getName() + " and the templates!");

                } else if (groupLoadedPacket.isProxy()) {

                    if (!(new File("templates/proxies/" + groupLoadedPacket.getName() + "/").exists())) {
                        new File("templates/proxies/" + groupLoadedPacket.getName() + "/").mkdirs();
                    }

                    groupLoadedPacket.getTemplates().forEach((templates) -> {
                        if (!(new File("templates/proxies/" + groupLoadedPacket.getName() + "/" + templates + "/" + "plugins/").exists())) {
                            new File("templates/proxies/" + groupLoadedPacket.getName() + "/" + templates + "/" + "plugins/").mkdirs();
                        }
                    });

                    this.wrapperModule.getProxyGroupManager().getProxyGroups().put(groupLoadedPacket.getName(), new ProxyGroup(groupLoadedPacket.getName(), groupLoadedPacket.getWrapper(), groupLoadedPacket.getVersion()));
                    this.wrapperModule.info("&bcreating new files for group " + groupLoadedPacket.getName() + " and the templates!");

                }

            } else if (packet.getKey().equals(PacketType.GroupCreatePacket.name())) {

                this.wrapperModule.info("received Packet " + packet.getKey());

                GroupCreatePacket groupCreatePacket = (GroupCreatePacket) packet.getObject();
                if (groupCreatePacket.getWrapperName().equals(this.wrapperModule.getName())) {

                    if (groupCreatePacket.getStatic().equals(false)) {

                        if (groupCreatePacket.getVersion().equals(VersionUtil.PAPER_1_19.name())) {
                            //templates/servers/

                            if (!(new File("templates/servers/" + groupCreatePacket.getGroupName() + "/").exists())) {
                                new File("templates/servers/" + groupCreatePacket.getGroupName() + "/").mkdirs();
                            }

                            if (!(new File("templates/servers/" + groupCreatePacket.getGroupName() + "/default/").exists())) {
                                new File("templates/servers/" + groupCreatePacket.getGroupName() + "/default/").mkdirs();
                            }

                            this.wrapperModule.info("§fEine Neue Servergruppe wurde erstellt");

                        } else if (groupCreatePacket.getVersion().equals(VersionUtil.WATERFALL.name())) {
                            //templates/proxies/

                            if (!(new File("templates/proxies/" + groupCreatePacket.getGroupName() + "/").exists())) {
                                new File("templates/proxies/" + groupCreatePacket.getGroupName() + "/").mkdirs();
                            }

                            if (!(new File("templates/proxies/" + groupCreatePacket.getGroupName() + "/default/").exists())) {
                                new File("templates/proxies/" + groupCreatePacket.getGroupName() + "/default/").mkdirs();
                            }

                            this.wrapperModule.info("§fEine Neue Servergruppe wurde erstellt");

                            //TODO: DO IT IN TEMPLATE CREATION!
                            /*
                            if (!(new File("templates/proxies/" + groupCreatePacket.getGroupName() + "/plugins/").exists())) {
                                new File("templates/proxies/" + groupCreatePacket.getGroupName() + "/plugins/").mkdirs();
                            }*/

                        }

                    }


                } else if (packet.getKey().equals(PacketType.TemplateCreatePacket.name())) {
                    this.wrapperModule.info("received Packet " + packet.getKey());
                    TemplateCreatePacket templateCreatePacket = (TemplateCreatePacket) packet.getObject();

                }

            }

        }).start();

    }

}
