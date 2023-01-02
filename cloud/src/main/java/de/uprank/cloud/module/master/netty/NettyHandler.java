package de.uprank.cloud.module.master.netty;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.proxies.Proxy;
import de.uprank.cloud.module.master.wrapper.Wrapper;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.ErrorPacket;
import de.uprank.cloud.packets.type.group.GroupLoadedPacket;
import de.uprank.cloud.packets.type.proxy.ProxyAddGameServerPacket;
import de.uprank.cloud.packets.type.proxy.ProxyRemoveGameServerPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerStartPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerStartPacket;
import de.uprank.cloud.packets.type.wrapper.WrapperAlivePacket;
import de.uprank.cloud.packets.type.wrapper.WrapperStartPacket;
import de.uprank.cloud.packets.type.wrapper.WrapperStopPacket;
import de.uprank.cloud.packets.type.wrapper.WrapperStopServerPacket;
import de.uprank.cloud.util.VersionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NettyHandler extends SimpleChannelInboundHandler<Object> {

    private final MasterModule masterModule;

    public NettyHandler(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.setChannel(channelHandlerContext.channel());

        List<File> files = Arrays.asList(new File("groups/").listFiles());
        List<String> groups = new ArrayList<>();
        files.forEach((folders) -> groups.add(folders.getName().replace(".json", "")));

        groups.forEach((servergroups) -> {
            JsonParser jsonParser = new JsonParser();
            try {
                JsonObject jsonObjects = (JsonObject) jsonParser.parse(new FileReader("groups/" + servergroups + ".json"));
                JsonArray jsonArray = jsonObjects.getAsJsonArray("templates");

                for (int i = 0; i < jsonArray.size(); ++i) {

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    List<String> templates = new ArrayList<>();
                    templates.add(jsonObject.get("name").getAsString());
                    this.masterModule.getServergroups().put(servergroups, templates);

                    channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.GROUP_LOADED_PACKET.name(), new GroupLoadedPacket(servergroups, jsonObjects.get("isFallBack").getAsBoolean(), jsonObjects.get("version").getAsString().equals(VersionUtil.WATERFALL.name()) ? true : false, templates)));
                    templates.forEach((templatess) -> {
                        channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), new GameServerRequestPacket(servergroups, templatess, jsonObjects.get("wrapper").getAsString(), jsonObject.get("minMemory").getAsInt(), jsonObject.get("maxMemory").getAsInt(), jsonObjects.get("version").getAsString().equals(VersionUtil.WATERFALL.name()) ? true : false, jsonObjects.get("isFallBack").getAsBoolean(), true)));
                    });
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        new Thread(() -> {

            Packet packet = (Packet) object;

            if (packet.getKey().equals(PacketType.WrapperStartPacket.name())) {
                WrapperStartPacket wrapperStartPacket = (WrapperStartPacket) packet.getObject();
                this.masterModule.getWrapperManager().registerWrapper(wrapperStartPacket, channelHandlerContext.channel());

            } else if (packet.getKey().equals(PacketType.WrapperAlivePacket.name())) {
                WrapperAlivePacket wrapperAlivePacket = (WrapperAlivePacket) packet.getObject();
                Wrapper wrapper = this.masterModule.getWrapperManager().getWrapperbyName(wrapperAlivePacket.getName());
                if (wrapper != null) {
                    wrapper.setTimeout(System.currentTimeMillis() + 10000);
                    wrapper.setUsedMemory(wrapperAlivePacket.getUsedMemory());
                    wrapper.setFreeMemory(wrapperAlivePacket.getFreeMemory());
                    wrapper.setMaxMemory(wrapperAlivePacket.getMaxMemory());

                    wrapper.setCpuUsage(wrapperAlivePacket.getCpuUsage());
                    wrapper.setAverageCpu(wrapperAlivePacket.getAverageCpu());

                }
            } else if (packet.getKey().equals(PacketType.WrapperStopPacket.name())) {
                WrapperStopPacket wrapperStopPacket = (WrapperStopPacket) packet.getObject();
                Wrapper wrapper = this.masterModule.getWrapperManager().getWrapperbyName(wrapperStopPacket.getName());
                if (wrapper != null) {
                    this.masterModule.getWrapperManager().removeWrapperByName(wrapper.getName());
                }
            } else if (packet.getKey().equals(PacketType.ERROR.name())) {
                ErrorPacket errorPacket = (ErrorPacket) packet.getObject();

            } else if (packet.getKey().equals(PacketType.ProxyServerStartPacket.name())) {

                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxies().put(proxyServerStartPacket.getName(), new Proxy(proxyServerStartPacket.getName(), channelHandlerContext.channel()));

            } else if (packet.getKey().equals(PacketType.ProxyServerStopPacket.name())) {

                ProxyServerStartPacket proxyServerStartPacket = (ProxyServerStartPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxies().remove(proxyServerStartPacket.getName());

            } else if (packet.getKey().equals(PacketType.ProxyAddGameServerPacket.name())) {

                ProxyAddGameServerPacket gameServerStartPacket = (ProxyAddGameServerPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxies().forEach(((s, proxy) -> proxy.getChannel().writeAndFlush(packet)));

            } else if (packet.getKey().equals(PacketType.ProxyRemoveGameServerPacket.name())) {

                ProxyRemoveGameServerPacket proxyAddGameServerPacket = (ProxyRemoveGameServerPacket) packet.getObject();
                this.masterModule.getProxyManager().getProxies().forEach(((s, proxy) -> proxy.getChannel().writeAndFlush(packet)));

            }

        }).start();

    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.getWrapperManager().getWrapperList().forEach((wrappers) -> {
            channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.WrapperStopServerPacket.name(), new WrapperStopServerPacket(wrappers.getName())));
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
