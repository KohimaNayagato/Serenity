package dev.kohimanayagato.serenity.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class LoggerUtil
{
	public static void sendMessage(String message)
	{
		sendMessage(message, true);
	}

	public static void sendMessage(String message, boolean waterMark)
	{
		StringBuilder messageBuilder = new StringBuilder();
		if (waterMark) messageBuilder.append("[Serenity] ");
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(messageBuilder.toString().replace("&", "§")));
	}
}