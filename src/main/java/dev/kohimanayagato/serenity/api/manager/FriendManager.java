package dev.kohimanayagato.serenity.api.manager;

import dev.kohimanayagato.serenity.api.util.FriendUtil;

import java.util.ArrayList;

public class FriendManager
{
    private final ArrayList<FriendUtil> friends = new ArrayList<>();

    public void removeFriend(String name)
    {
        friends.stream().filter(friend -> friend.getName().equalsIgnoreCase(name)).forEach(friends::remove);
    }

    public FriendUtil addFriend(String name)
    {
        friends.add(new FriendUtil(name));
        return null;
    }

    public ArrayList<FriendUtil> getFriends()
    {
        return friends;
    }
}