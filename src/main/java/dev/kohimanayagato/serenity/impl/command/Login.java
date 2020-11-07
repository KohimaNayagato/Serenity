package dev.kohimanayagato.serenity.impl.command;

import dev.kohimanayagato.serenity.api.command.Command;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import dev.kohimanayagato.serenity.api.util.SessionUtil;


public class Login extends Command {
	public Login(String name, String[] alias, String usage) {
		super(name, alias, usage);
	}

	@Override
	public void onTrigger(String arguments) {
		String[] split = arguments.split(" ");
		try {
			if (split[0].equals("") || split[1].equals("")) {
				printUsage();
				return;
			}
		} catch (Exception ignored) {
			printUsage();
			return;
		}

		if (SessionUtil.login(split[0], split[1])) {
			LoggerUtil.sendMessage("Logged in to " + SessionUtil.getSession().getUsername());
		} else {
			LoggerUtil.sendMessage("Failed to log in");
		}
	}

	public void onRun(String arguments) {

	}
}