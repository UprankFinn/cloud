package de.uprank.cloud.module.friend.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import de.uprank.cloud.module.friend.FriendModule;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class FriendPlayerManager {

    private final FriendModule friendModule;

    private final Gson gson;
    private final MongoCollection mongoCollection;

    private final Map<UUID, FriendPlayer> friends;

    public FriendPlayerManager(FriendModule friendModule) {
        this.friendModule = friendModule;

        this.gson = new GsonBuilder().create();
        this.mongoCollection = this.friendModule.getMongoClient().getDatabase("profiles").getCollection("friend_Profiles");

        this.friends = new HashMap<>();

        this.mongoCollection.find().forEach((Consumer<Document>) document -> {
            FriendPlayer friendPlayers = this.gson.fromJson(this.gson.toJson(document), FriendPlayer.class);
            this.friends.put(friendPlayers.getUniqueId(), friendPlayers);
        });

    }

    public FriendPlayer getFriendPlayer(UUID uniqueId) {
        if (this.friends.containsKey(uniqueId)) return this.friends.get(uniqueId);
        return null;
    }

    public Map<UUID, FriendPlayer> getFriends() {
        return friends;
    }

}
