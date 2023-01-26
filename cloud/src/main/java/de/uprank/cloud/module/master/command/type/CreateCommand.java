package de.uprank.cloud.module.master.command.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.module.master.command.Command;
import de.uprank.cloud.packets.Packet;
import de.uprank.cloud.packets.PacketType;
import de.uprank.cloud.packets.type.group.GroupCreatePacket;
import de.uprank.cloud.packets.type.group.GroupLoadedPacket;
import de.uprank.cloud.packets.type.group.template.TemplateCreatePacket;
import de.uprank.cloud.util.VersionUtil;

import java.io.*;
import java.util.Arrays;

public class CreateCommand implements Command {

    private final MasterModule masterModule;

    public CreateCommand(MasterModule masterModule) {
        this.masterModule = masterModule;
    }

    @Override
    public void execute(String command, String[] args) {

        /*

        servergroup <name> <wrapper>
        template <group> <name> <wrapper>

         */

        if (args.length == 0) {
            this.masterModule.info("&cYou can use the following parameters for this command:");
            this.masterModule.info("  &8» &bcreate servergroup &8(&bname&8) &8(&bwrapper&8) &8(&bVersion&8) &8(&bisFallback&8) &8- &rcreate a servergroup on a wrapper");
            this.masterModule.info("  &8» &bcreate proxygroup &8(&bname&8) &8(&bwrapper&8) &8(&bVersion&8) &8(&bisFallback&8) &8- &rcreate a servergroup on a wrapper");
            this.masterModule.info("  &8» &bcreate template &8(&bgroup&8) &8(&bname&8) &8(&bwrapper&8) &8(&bmaxPlayers&8) &8(&bmimMemory&8) &8(&bmaxMemory&8) &8(&bminServices&8) &8(&bmaxServices&8) &8- &rcreate a template for a servergroup on a wrapper");
            return;
        } else if (args.length == 5) {

            if (args[0].equalsIgnoreCase("proxygroup")) {
                String group = args[1];
                String wrapper = args[2];
                String version = args[3];
                Boolean fallback = Boolean.valueOf(args[4]);

                if (group == null) {
                    this.masterModule.warning("&cBitte gebe einen gültigen Namen an!");
                    return;
                }

                if (this.masterModule.getWrapperManager().getWrapperbyName(wrapper) == null) {
                    this.masterModule.warning("&cBitte gebe einen gültigen Wrapper an!");
                    return;
                }

                if (!(VersionUtil.exists(version))) {
                    this.masterModule.warning("&cBitte gebe eine gültige Version an! Folgende Versionen stehen zur auswahl:");
                    for (VersionUtil value : VersionUtil.values()) {
                        this.masterModule.warning("  &8» &b" + value.name());
                    }
                    return;
                }

                if (fallback == null) {
                    this.masterModule.warning("&cBitte gebe für den FallBack einen gültigen Wert an! &8(&btrue&8/&bfalse&8)");
                    return;
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", group);
                jsonObject.addProperty("wrapper", wrapper);
                jsonObject.addProperty("version", version);
                jsonObject.addProperty("isFallBack", fallback);

                JsonArray jsonArray = new JsonArray();

                JsonObject insert = new JsonObject();
                insert.addProperty("name", "default");
                insert.addProperty("minMemory", 256);
                insert.addProperty("maxMemory", 512);
                insert.addProperty("minOnline", 1);
                insert.addProperty("maxOnline", -1);
                jsonArray.add(insert);
                jsonObject.add("templates", jsonArray);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                try (FileWriter fileWriter = new FileWriter("groups/proxies/" + group + ".json")) {
                    gson.toJson(jsonObject, fileWriter);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                this.masterModule.getChannel().writeAndFlush(new Packet(PacketType.GroupCreatePacket.name(), new GroupCreatePacket(group, wrapper, false, fallback, version)));
                this.masterModule.getChannel().writeAndFlush(new Packet(PacketType.GROUP_LOADED_PACKET.name(), new GroupLoadedPacket(group, wrapper, jsonObject.get("version").getAsString(), jsonObject.get("isFallBack").getAsBoolean(), jsonObject.get("version").equals(VersionUtil.WATERFALL.name()) ? true : false, Arrays.asList(insert.get("name").getAsString()))));
                this.masterModule.info("&bSuccessfully created new Group &e" + group + " &bon Wrapper &e" + wrapper + " &7:)");

            } else if (args[0].equalsIgnoreCase("servergroup")) {
                String group = args[1];
                String wrapper = args[2];
                String version = args[3];
                Boolean fallback = Boolean.valueOf(args[4]);

                if (group == null) {
                    this.masterModule.warning("&cBitte gebe einen gültigen Namen an!");
                    return;
                }

                if (this.masterModule.getWrapperManager().getWrapperbyName(wrapper) == null) {
                    this.masterModule.warning("&cBitte gebe einen gültigen Wrapper an!");
                    return;
                }

                if (!(VersionUtil.exists(version))) {
                    this.masterModule.warning("&cBitte gebe eine gültige Version an! Folgende Versionen stehen zur auswahl:");
                    for (VersionUtil value : VersionUtil.values()) {
                        this.masterModule.warning("  &8» &b" + value.name());
                    }
                    return;
                }

                if (fallback == null) {
                    this.masterModule.warning("&cBitte gebe für den FallBack einen gültigen Wert an! &8(&btrue&8/&bfalse&8)");
                    return;
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", group);
                jsonObject.addProperty("wrapper", wrapper);
                jsonObject.addProperty("version", version);
                jsonObject.addProperty("isFallBack", fallback);

                JsonArray jsonArray = new JsonArray();

                JsonObject insert = new JsonObject();
                insert.addProperty("name", "default");
                insert.addProperty("minMemory", 256);
                insert.addProperty("maxMemory", 512);
                insert.addProperty("minOnline", 1);
                insert.addProperty("maxOnline", -1);
                jsonArray.add(insert);
                jsonObject.add("templates", jsonArray);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                try (FileWriter fileWriter = new FileWriter("groups/servers/" + group + ".json")) {
                    gson.toJson(jsonObject, fileWriter);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                this.masterModule.getChannel().writeAndFlush(new Packet(PacketType.GroupCreatePacket.name(), new GroupCreatePacket(group, wrapper, false, fallback, version)));
                this.masterModule.getChannel().writeAndFlush(new Packet(PacketType.GROUP_LOADED_PACKET.name(), new GroupLoadedPacket(group, wrapper, jsonObject.get("version").getAsString(), jsonObject.get("isFallBack").getAsBoolean(), jsonObject.get("version").equals(VersionUtil.WATERFALL.name()) ? true : false, Arrays.asList(insert.get("name").getAsString()))));
                this.masterModule.info("&bSuccessfully created new Group &e" + group + " &bon Wrapper &e" + wrapper + " &7:)");

            }

        } else if (args.length == 9) {

            if (args[0].equals("template")) {
                String group = args[1];
                String name = args[2];
                String wrapper = args[3];
                Integer minMemory = Integer.valueOf(args[4]);
                Integer maxMemory = Integer.valueOf(args[5]);
                Integer maxPlayers = Integer.valueOf(args[6]);
                Integer minServices = Integer.valueOf(args[7]);
                Integer maxServices = Integer.valueOf(args[8]);

                if (group == null) {
                    this.masterModule.warning("&cBitte gebe eine gültige Gruppe an!");
                    return;
                }

                if (name == null) {
                    this.masterModule.warning("&cBitte gebe einen gültigen Namen an!");
                    return;
                }

                if (this.masterModule.getWrapperManager().getWrapperbyName(wrapper) == null) {
                    this.masterModule.warning("&cBitte gebe einen gültigen Wrapper an!");
                    return;
                }

                if (minMemory == null) {
                    this.masterModule.warning("&cBitte gebe eine gültige Zahl an!");
                    return;
                }

                if (maxMemory == null) {
                    this.masterModule.warning("&cBitte gebe eine gültige Zahl an!");
                    return;
                }

                if (maxPlayers == null || maxPlayers == 0) {
                    this.masterModule.warning("&cBitte gebe eine gültige Zahl an!");
                    return;
                }

                if (minServices == null) {
                    this.masterModule.warning("&cBitte gebe eine gültige Zahl an!");
                    return;
                }

                if (maxServices == null) {
                    this.masterModule.warning("&cBitte gebe eine gültige Zahl an!");
                    return;
                }

                this.masterModule.getChannel().writeAndFlush(new Packet(PacketType.TemplateCreatePacket.name(), new TemplateCreatePacket(name, group, minMemory, maxMemory)));

                this.masterModule.info("&bSuccessfully created new Template for Group &e" + group + " &bon Wrapper &e" + wrapper + " &7:)");

            }

        }

    }
}
