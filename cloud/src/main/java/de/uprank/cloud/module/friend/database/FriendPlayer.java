package de.uprank.cloud.module.friend.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.uprank.cloud.module.friend.FriendModule;

import java.util.List;
import java.util.UUID;

public class FriendPlayer {

    private final UUID uniqueId;
    private final String name;

    private final List<UUID> friends;
    private final List<UUID> requests;

    public FriendPlayer(UUID uniqueId, String name, List<UUID> friends, List<UUID> requests) {
        this.uniqueId = uniqueId;
        this.name = name;

        this.friends = friends;
        this.requests = requests;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public void addFriend(UUID uniqueId) {
        this.friends.add(uniqueId);
        FriendModule.getInstance().getMongoClient().getDatabase("profiles").getCollection("friend_Profiles").updateOne(
                Filters.eq("uniqueId", this.uniqueId.toString()), Updates.set("friends", friends));
    }

    public void removeFriend(UUID uniqueId) {
        this.friends.remove(uniqueId);
        FriendModule.getInstance().getMongoClient().getDatabase("profiles").getCollection("friend_Profiles").updateOne(
                Filters.eq("uniqueId", this.uniqueId.toString()), Updates.set("friends", friends));
    }

    public List<UUID> getRequests() {
        return requests;
    }

    public void addRequest(UUID uniqueId){
        this.requests.add(uniqueId);
        FriendModule.getInstance().getMongoClient().getDatabase("profiles").getCollection("friend_Profiles").updateOne(
                Filters.eq("uniqueId", this.uniqueId.toString()), Updates.set("requests", requests));
    }

    public void removeRequest(UUID uniqueId){
        this.requests.remove(uniqueId);
        FriendModule.getInstance().getMongoClient().getDatabase("profiles").getCollection("friend_Profiles").updateOne(
                Filters.eq("uniqueId", this.uniqueId.toString()), Updates.set("requests", requests));
    }

}
