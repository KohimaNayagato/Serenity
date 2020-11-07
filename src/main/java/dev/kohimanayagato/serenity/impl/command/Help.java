package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;

public class Help extends Command
{
	public Help(String name, String[] alias, String usage)
	{
		super(name, alias, usage);
	}

	@Override
	public void onTrigger(String arguments)
	{
		LoggerUtil.sendMessage("Serenity 0.4");

		for (Command command : Client.commandManager.getCommands())
		{
			LoggerUtil.sendMessage(command.getName() + " - " + command.getUsage());
		}
	}

	public void onRun(String arguments) {

	}

}
