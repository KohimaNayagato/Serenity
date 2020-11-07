package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import dev.kohimanayagato.serenity.api.util.font.CustomFontRenderer;
import dev.kohimanayagato.serenity.api.util.font.FontUtil;


public class Font extends Command
{
	public Font(String name, String[] alias, String usage)
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

		if (FontUtil.validateFont(arguments))
		{
			try
			{
				Client.customFontRenderer = new CustomFontRenderer(new java.awt.Font(arguments, java.awt.Font.PLAIN, 19), true, false);
				LoggerUtil.sendMessage("New font set to " + arguments);
			}
			catch (Exception e)
			{
				LoggerUtil.sendMessage("Failed to set font");
			}
		}
		else
		{
			LoggerUtil.sendMessage("Invalid font");
		}
	}

	public void onRun(String arguments) {

	}
}
