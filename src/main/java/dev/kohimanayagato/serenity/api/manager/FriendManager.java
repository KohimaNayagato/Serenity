package dev.kohimanayagato.serenity.api.manager;

import dev.kohimanayagato.serenity.api.util.FriendUtil;

import java.util.ArrayList;

public class FriendManager
{
    private final ArrayList<FriendUtil> friends = new ArrayList<>();

    public ArrayList<FriendUtil> getFriends()
    {
        return friends;
    }

    public void addFriend(String name)
    {
        friends.add(new FriendUtil(name));
    }

    public void delFriend(String name)
    {
        friends.remove(getFriendByName(name));
    }

    public FriendUtil getFriendByName(String name)
    {
        for (FriendUtil friend : getFriends())
        {
            if (friend.getName().equalsIgnoreCase(name))
            {
                return friend;
            }
        }
        return null;
    }

    public boolean isFriend(String name)
    {
        for (FriendUtil friend : getFriends())
        {
            if (friend.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getFriendsForConfig()
    {
        ArrayList<String> arr = new ArrayList<>();

        for (FriendUtil friend : getFriends())
        {
            arr.add(friend.getName());
        }
        return arr;
    }
}