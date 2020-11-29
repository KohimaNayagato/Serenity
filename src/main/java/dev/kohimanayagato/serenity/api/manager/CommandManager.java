package dev.kohimanayagato.serenity.api.manager;

import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import dev.kohimanayagato.serenity.impl.command.*;

import java.util.ArrayList;


public class CommandManager
{
	private final ArrayList<Command> commands = new ArrayList<>();
	private String prefix = ":";

	public CommandManager()
	{
		commands.add(new Help("Help", new String[]{"h", "help"}, "help"));
		commands.add(new Prefix("Prefix", new String[]{"prefix"}, "prefix <char>"));
		commands.add(new Login("Login", new String[]{"login"}, "login <email> <password>"));
		commands.add(new Font("Font", new String[]{"font"}, "font <font>"));
		commands.add(new Bind("Bind", new String[]{"bind"}, "bind <module> <key>"));
		commands.add(new Toggle("Toggle", new String[]{"toggle"}, "toggle <module>"));
		commands.add(new Set("Set", new String[]{"set"}, "set <module> <setting> <value>"));
		commands.add(new Friend("Friend", new String[]{"friend"}, "friend <add/del/list> <name>"));
	}

	public void runCommand(String args)
	{
		boolean found = false;
		String[] split = args.split(" ");
		String startCommand = split[0];
		String arguments = args.substring(startCommand.length()).trim();

		for (Command command : getCommands())
		{
			for (String alias : command.getAlias())
			{
				if (startCommand.equals(getPrefix() + alias))
				{
					command.onTrigger(arguments);
					found = true;
				}
			}
		}

		if (!found)
		{
			LoggerUtil.sendMessage("Unknown command");
		}
	}

	public ArrayList<Command> getCommands()
	{
		return commands;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
}
