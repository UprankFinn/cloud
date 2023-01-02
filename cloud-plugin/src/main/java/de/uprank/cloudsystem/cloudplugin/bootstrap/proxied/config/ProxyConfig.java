package de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.config;

import com.google.gson.*;
import de.uprank.cloudsystem.cloudplugin.bootstrap.proxied.CloudProxiedPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProxyConfig {

    private final CloudProxiedPlugin plugin;

    private String prefix;
    private Integer slots;

    private String line_1;
    private List<String> line_2 = new ArrayList<>();

    private Boolean maintenance;
    private String maintenancePermission;
    private String maintenanceVersionControl;

    private String maintenanceLine_1;
    private List<String> maintenance_Lines_2 = new ArrayList<>();

    private String maintenanceKickMessage;

    private Boolean notify = true;
    private String notifyPermission = "de.uprank.cloudproxy.notify";
    private String notifyServiceStart = "§bThe Service %service% is now starting!";
    private String notifyServiceStop = "§bThe Service %service% is now stopping!";

    private String fallBackGroup;
    private Boolean hubCommandUse;
    private String hubCommandUseMessage;

    private Boolean premiumLobby;
    private String premiumLobbyGroup;
    private String premiumLobbyGroupMessage;

    private Boolean silentlobby;
    private String silentlobbyGroup;
    private String silentlobbyGroupMessage;

    public ProxyConfig(CloudProxiedPlugin plugin) {
        this.plugin = plugin;

        if (!(new File("plugins/CloudProxy/").exists())) {
            new File("plugins/CloudProxy/").mkdirs();
        }

        if (!(new File("plugins/CloudProxy/config.json")).exists()) {
            JsonObject jsonObject = new JsonObject();

            //-----------------------------[PREFIX]-----------------------------

            jsonObject.addProperty("prefix", "§bCloudSystem §8» §r");
            jsonObject.addProperty("slots", 1000);

            //-----------------------------[MOTD]-----------------------------

            JsonObject motdObject = new JsonObject();

            motdObject.addProperty("line_1", "§bA default CloudServer :)");
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("§bYou can change me in your bungeecord config :)");
            jsonArray.add("§bfollow me on Twitter: @UprankFinn");
            jsonArray.add("§benjoy the cloudsystem :)");
            motdObject.add("line_2", jsonArray);

            //-----------------------------[Maintenance]-----------------------------

            JsonObject maintenanceObject = new JsonObject();

            maintenanceObject.addProperty("maintenance", true);
            maintenanceObject.addProperty("maintenance_Permission", "de.uprank.cloudproxy.maintenance.bypass");
            maintenanceObject.addProperty("maintenance_VersionControl", "§cMaintenance");

            JsonObject maintenanceMotdObject = new JsonObject();
            maintenanceMotdObject.addProperty("line_1", "§bA default CloudServer :)");
            JsonArray maintenanceArray = new JsonArray();
            maintenanceArray.add("§bYou can change me in your bungeecord config :)");
            maintenanceArray.add("§bfollow me on Twitter: @UprankFinn");
            maintenanceArray.add("§benjoy the cloudsystem :)");

            JsonArray maintenanceKickMessage = new JsonArray();
            maintenanceKickMessage.add("§cYou have been kicked because of maintenance work.");

            maintenanceMotdObject.add("line_2", maintenanceArray);
            maintenanceObject.add("maintenanceMotd", maintenanceMotdObject);
            maintenanceObject.add("maintenance_kick_message", maintenanceKickMessage);

            //-----------------------------[ServiceNotify]-----------------------------

            JsonObject serviceObject = new JsonObject();

            serviceObject.addProperty("notify", true);
            serviceObject.addProperty("notify_Permission", "de.uprank.cloudproxy.notify");
            serviceObject.addProperty("notify_Message_Start", "§bThe Service %service% is now starting!");
            serviceObject.addProperty("notify_Message_Stop", "§bThe Service %service% is now stopping!");

            //-----------------------------[fallBack]-----------------------------

            JsonObject fallBackObject = new JsonObject();

            fallBackObject.addProperty("fallBack_Group", "Lobby");
            fallBackObject.addProperty("hubCommand_use", true);
            fallBackObject.addProperty("hubCommand_Message_use", "§7Du wurdest auf die Lobby gesendet!");
            fallBackObject.addProperty("premiumLobby", false);
            fallBackObject.addProperty("premiumLobby_Group", "");
            fallBackObject.addProperty("premiumLobby_Permission", "de.uprank.cloudproxy.use.premiumlobby");
            fallBackObject.addProperty("silentLobby", false);
            fallBackObject.addProperty("silentLobby_Group", "");
            fallBackObject.addProperty("silentLobby_Permission", "de.uprank.cloudproxy.use.silentlobby");

            //-----------------------------[Config]-----------------------------

            jsonObject.add("motd", motdObject);
            jsonObject.add("maintenance", maintenanceObject);
            jsonObject.add("service_notification", serviceObject);
            jsonObject.add("fallBack", fallBackObject);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter fileWriter = new FileWriter("plugins/CloudProxy/config.json")) {
                gson.toJson(jsonObject, fileWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readConfig() {

        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader("plugins/CloudProxy/config.json"));

            //-----------------------------[PREFIX]-----------------------------

            this.prefix = jsonObject.get("prefix").getAsString();
            this.slots = jsonObject.get("slots").getAsInt();

            //-----------------------------[MOTD]-----------------------------

            JsonObject motdObject = jsonObject.get("motd").getAsJsonObject();

            this.line_1 = motdObject.get("line_1").getAsString();
            JsonArray jsonArray = motdObject.getAsJsonArray("line_2");
            jsonArray.forEach((lines) -> this.line_2.add(String.valueOf(lines)));

            //-----------------------------[Maintenance]-----------------------------

            JsonObject maintenanceObject = jsonObject.getAsJsonObject("maintenance");

            this.maintenance = maintenanceObject.get("maintenance").getAsBoolean();
            this.maintenancePermission = maintenanceObject.get("maintenance_Permission").getAsString();
            this.maintenanceVersionControl = maintenanceObject.get("maintenance_VersionControl").getAsString();

            JsonObject maintenanceMotdObject = maintenanceObject.get("maintenanceMotd").getAsJsonObject();
            this.line_1 = maintenanceMotdObject.get("line_1").getAsString();

            JsonArray maintenanceMotdLine2 = maintenanceMotdObject.getAsJsonArray("line_2");
            maintenanceMotdLine2.forEach((lines) -> this.maintenance_Lines_2.add(String.valueOf(lines)));

            JsonArray maintenancekickObject = maintenanceObject.get("maintenance_kick_message").getAsJsonArray();
            this.maintenanceKickMessage = maintenancekickObject.getAsString();

            //-----------------------------[ServiceNotify]-----------------------------


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public String getPrefix() {
        return prefix;
    }

    public Integer getSlots() {
        return slots;
    }

    public String getLine_1() {
        return line_1;
    }

    public List<String> getLine_2() {
        return line_2;
    }

    public Boolean isMaintenance() {
        return maintenance;
    }

    public String getMaintenancePermission() {
        return maintenancePermission;
    }

    public String getMaintenanceVersionControl() {
        return maintenanceVersionControl;
    }

    public String getMaintenanceLine_1() {
        return maintenanceLine_1;
    }

    public List<String> getMaintenance_Lines_2() {
        return maintenance_Lines_2;
    }

    public String getMaintenanceKickMessage() {
        return maintenanceKickMessage;
    }

    public Boolean isNotify() {
        return notify;
    }

    public String getNotifyPermission() {
        return notifyPermission;
    }

    public String getNotifyServiceStart() {
        return notifyServiceStart;
    }

    public String getNotifyServiceStop() {
        return notifyServiceStop;
    }

    public String getFallBackGroup() {
        return fallBackGroup;
    }

    public Boolean isHubCommandActive() {
        return hubCommandUse;
    }

    public String getHubCommandUseMessage() {
        return hubCommandUseMessage;
    }

    public Boolean isPremiumLobbyActive() {
        return premiumLobby;
    }

    public String getPremiumLobbyGroup() {
        return premiumLobbyGroup;
    }

    public String getPremiumLobbyGroupMessage() {
        return premiumLobbyGroupMessage;
    }

    public Boolean isSilentLobbyActive() {
        return silentlobby;
    }

    public String getSilentlobbyGroup() {
        return silentlobbyGroup;
    }

    public String getSilentlobbyGroupMessage() {
        return silentlobbyGroupMessage;
    }
}
