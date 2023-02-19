package de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.database.signs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.uprank.cloudsystem.cloudplugin.bootstrap.bukkit.CloudBukkitPlugin;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class CloudSignManager {

    private final CloudBukkitPlugin plugin;

    private final Gson gson;
    private final MongoCollection mongoCollection;

    private final Map<UUID, CloudSign> cloudSigns;
    private final Map<String, CloudSign> activeCloudSigns;

    public CloudSignManager(CloudBukkitPlugin plugin) {
        this.plugin = plugin;

        this.gson = new GsonBuilder().create();
        this.mongoCollection = this.plugin.getMongoClient().getDatabase("cloud").getCollection("cloudSigns");

        this.cloudSigns = new HashMap<>();
        this.activeCloudSigns = new HashMap<>();

        this.mongoCollection.find().forEach((Consumer<Document>) document -> {
            CloudSign signs = this.gson.fromJson(this.gson.toJson(document), CloudSign.class);
            this.cloudSigns.put(signs.getUniqueId(), signs);
        });

        this.cloudSigns.forEach((s, cloudSign) -> {
            final Location location = new Location(Bukkit.getWorld(cloudSign.getWorld()), cloudSign.getX(), cloudSign.getY(), cloudSign.getZ(), cloudSign.getYaw(), cloudSign.getPitch());

            if (location.getBlock().getType() == Material.OAK_WALL_SIGN) {
                Sign sign = (Sign) location.getBlock().getState();

                sign.setLine(0, "");
                sign.setLine(1, "searching");
                sign.setLine(2, "Server");
                sign.setLine(3, "");

                sign.update();

            }

        });

    }

    public void createCloudSign(CloudSign cloudSign) {
        if (this.mongoCollection.find(Filters.eq("uniqueId", cloudSign.getUniqueId().toString())).first() == null) {
            this.mongoCollection.insertOne(this.gson.fromJson(this.gson.toJson(cloudSign), Document.class));
            this.cloudSigns.put(cloudSign.getUniqueId(), cloudSign);
        }
    }

    public void deleteCloudSign(UUID uniqueId) {
        if (this.mongoCollection.find(Filters.eq("uniqueId", uniqueId.toString())).first() != null) {
            this.mongoCollection.deleteOne(Filters.eq("uniqueId", uniqueId.toString()));
            this.cloudSigns.remove(uniqueId);
        }
    }

    public CloudSign getCloudSign(UUID uniqueId) {
        if (this.cloudSigns.containsKey(uniqueId)) return this.cloudSigns.get(uniqueId);
        return null;
    }

    public CloudSign getCloudSign(Location location) {
        for (CloudSign cloudSign : this.cloudSigns.values()) {
            final Location locations = new Location(Bukkit.getWorld(cloudSign.getWorld()), cloudSign.getX(), cloudSign.getY(), cloudSign.getZ(), cloudSign.getYaw(), cloudSign.getPitch());
            if (locations.equals(location)) {
                return cloudSign;
            }
        }
        return null;
    }

}
