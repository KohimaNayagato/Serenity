package dev.kohimanayagato.serenity.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class LoggerUtil
{
	public static void sendMessage(String message)
	{
		sendMessage(message, false);
	}

	public static void sendMessage(String message, boolean waterMark)
	{
		StringBuilder messageBuilder = new StringBuilder();
		if (waterMark) messageBuilder.append("§7[§9Serenity§7] ");
		messageBuilder.append("").append(message);
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(messageBuilder.toString().replace("&", "§")));
	}
}