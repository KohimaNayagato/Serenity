package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.util.FriendUtil;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;

public class Friend extends Command
{
    public Friend(String name, String[] alias, String usage)
    {
        super(name, alias, usage);
    }

    @Override
    public void onTrigger(String arguments) {
        super.onTrigger(arguments);
    }

    @Override
    public void onRun(String arguments) {

    }

    public void run(String arguments)
    {
        String[] split = arguments.split(" ");


        if (split[0].equalsIgnoreCase("add"))
        {
            if (Client.friendManager.isFriend(split[1]))
            {
                LoggerUtil.sendMessage("User is already a friend");
            }
            else
            {
                Client.friendManager.addFriend(split[1]);
                LoggerUtil.sendMessage("Added " + split[1]);
            }
            return;
        }
        if (split[0].equalsIgnoreCase("remove") || split[0].equalsIgnoreCase("del"))
        {
            if (Client.friendManager.isFriend(split[1]))
            {
                Client.friendManager.delFriend(split[1]);
                LoggerUtil.sendMessage("Removed " + split[1]);
            }
            else
            {
                LoggerUtil.sendMessage("User is not your friend");
            }
            return;
        }
        if (split[0].equalsIgnoreCase("list"))
        {
            if (Client.friendManager.getFriends().size() == 0)
            {
                LoggerUtil.sendMessage("No friends");
                return;
            }
            for (FriendUtil friend : Client.friendManager.getFriends())
            {
                LoggerUtil.sendMessage(friend.getName());
            }
            return;
        }

        printUsage();
    }
}