package de.uprank.cloud.module.master.netty.wrapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.wrapper.Wrapper;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.ErrorPacket;
import de.uprank.cloud.packets.type.group.GroupLoadedPacket;
import de.uprank.cloud.packets.type.proxy.ProxyServerRequestPacket;
import de.uprank.cloud.packets.type.server.GameServerRequestPacket;
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

public class WrapperHandler extends SimpleChannelInboundHandler<Object> {

    private final MasterModule masterModule;

    public WrapperHandler(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.setWrapperChannel(channelHandlerContext.channel());

        this.initializeProxyGroups(channelHandlerContext);
        this.initializeServerGroups(channelHandlerContext);

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
                    wrapper.setChannel(channelHandlerContext.channel());

                    wrapper.setCpuUsage(wrapperAlivePacket.getCpuUsage());
                    wrapper.setAverageCpu(wrapperAlivePacket.getAverageCpu());

                }
            } else if (packet.getKey().equals(PacketType.WrapperStopPacket.name())) {
                WrapperStopPacket wrapperStopPacket = (WrapperStopPacket) packet.getObject();
                Wrapper wrapper = this.masterModule.getWrapperManager().getWrapperbyName(wrapperStopPacket.getName());
                if (wrapper != null) {
                    this.masterModule.getWrapperManager().removeWrapperByName(wrapper.getName());
                }

            }

        }).start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.masterModule.getWrapperManager().getWrapperList().forEach((wrappers) -> {
            channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.WrapperStopServerPacket.name(), new WrapperStopServerPacket(wrappers.getName())));
        });
    }

    public void initializeProxyGroups(ChannelHandlerContext channelHandlerContext) {
        List<File> files = Arrays.asList(new File("groups/proxies/").listFiles());
        List<String> groups = new ArrayList<>();
        files.forEach((folders) -> groups.add(folders.getName().replace(".json", "")));

        groups.forEach((proxyGroups) -> {
            JsonParser jsonParser = new JsonParser();

            try {
                JsonObject jsonObjects = (JsonObject) jsonParser.parse(new FileReader("groups/proxies/" + proxyGroups + ".json"));
                JsonArray jsonArray = jsonObjects.getAsJsonArray("templates");

                for (int i = 0; i < jsonArray.size(); ++i) {

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    List<String> templates = new ArrayList<>();
                    templates.add(jsonObject.get("name").getAsString());
                    this.masterModule.getServergroups().put(proxyGroups, templates);

                    channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.GROUP_LOADED_PACKET.name(), new GroupLoadedPacket(proxyGroups, jsonObjects.get("wrapper").getAsString(), jsonObjects.get("version").getAsString(), jsonObjects.get("isFallBack").getAsBoolean(), jsonObjects.get("version").getAsString().equals(VersionUtil.WATERFALL.name()) ? true : false, templates)));
                    templates.forEach((templatess) -> this.masterModule.getProxyChannel().writeAndFlush(new Packet(PacketType.ProxyServerRequestPacket.name(), new ProxyServerRequestPacket(proxyGroups, templatess, jsonObjects.get("wrapper").getAsString(), jsonObject.get("minMemory").getAsInt(), jsonObject.get("maxMemory").getAsInt(),
                            jsonObjects.get("version").getAsString().equals(VersionUtil.WATERFALL.name()) ? true : false, true))));
                }

                this.masterModule.info("loaded ProxyGroup &e" + proxyGroups);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initializeServerGroups(ChannelHandlerContext channelHandlerContext) {
        List<File> files = Arrays.asList(new File("groups/servers/").listFiles());
        List<String> groups = new ArrayList<>();
        files.forEach((folders) -> groups.add(folders.getName().replace(".json", "")));

        groups.forEach((servergroups) -> {
            JsonParser jsonParser = new JsonParser();

            try {
                JsonObject jsonObjects = (JsonObject) jsonParser.parse(new FileReader("groups/servers/" + servergroups + ".json"));
                JsonArray jsonArray = jsonObjects.getAsJsonArray("templates");

                for (int i = 0; i < jsonArray.size(); ++i) {

                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    List<String> templates = new ArrayList<>();
                    templates.add(jsonObject.get("name").getAsString());
                    this.masterModule.getServergroups().put(servergroups, templates);

                    channelHandlerContext.channel().writeAndFlush(new Packet(PacketType.GROUP_LOADED_PACKET.name(), new GroupLoadedPacket(servergroups, jsonObjects.get("wrapper").getAsString(), jsonObjects.get("version").getAsString(), jsonObjects.get("isFallBack").getAsBoolean(), jsonObjects.get("version").getAsString().equals(VersionUtil.WATERFALL.name()) ? true : false, templates)));
                    templates.forEach((templatess) -> {

                        this.masterModule.getServerChannel().writeAndFlush(new Packet(PacketType.GameServerRequestPacket.name(), new GameServerRequestPacket(servergroups, templatess, jsonObjects.get("wrapper").getAsString(), jsonObject.get("minMemory").getAsInt(), jsonObject.get("maxMemory").getAsInt(), jsonObjects.get("version").getAsString().equals(VersionUtil.WATERFALL.name()) ? true : false, jsonObjects.get("isFallBack").getAsBoolean(), true)));

                    });
                }

                this.masterModule.info("loaded ServerGroup &e" + servergroups);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
