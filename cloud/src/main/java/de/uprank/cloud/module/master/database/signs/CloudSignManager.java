package de.uprank.cloud.module.master.database.signs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.uprank.cloud.module.master.MasterModule;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class CloudSignManager {

    private final MasterModule masterModule;

    private final Gson gson;
    private final MongoCollection mongoCollection;

    private final Map<UUID, CloudSign> cloudSigns;
    private final Map<String, CloudSign> activeCloudSigns;

    public CloudSignManager(MasterModule masterModule) {
        this.masterModule = masterModule;

        this.gson = new GsonBuilder().create();
        this.mongoCollection = this.masterModule.getMongoClient().getDatabase("cloud").getCollection("cloudSigns");

        this.cloudSigns = new HashMap<>();
        this.activeCloudSigns = new HashMap<>();

        this.mongoCollection.find().forEach((Consumer<Document>) document -> {
            CloudSign signs = this.gson.fromJson(this.gson.toJson(document), CloudSign.class);
            this.cloudSigns.put(signs.getUniqueId(), signs);
        });

    }

    public CloudSign getCloudSign(UUID uniqueId) {
        if (this.cloudSigns.containsKey(uniqueId)) return this.cloudSigns.get(uniqueId);
        return null;
    }

    public Map<UUID, CloudSign> getCloudSigns() {
        return cloudSigns;
    }
}
