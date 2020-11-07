package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;


public class Prefix extends Command
{
	public Prefix(String name, String[] alias, String usage)
	{
		super(name, alias, usage);
	}

	@Override
	public void onTrigger(String arguments)
	{
		if (arguments.equals(""))
		{
			printUsage();
			return;
		}

		Client.commandManager.setPrefix(arguments);
		LoggerUtil.sendMessage("Prefix set to " + arguments);
	}

	public void onRun(String arguments) {

	}
}
