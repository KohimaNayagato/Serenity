package dev.kohimanayagato.serenity.impl.module.misc;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class ChatSuffix extends Module
{
	public ChatSuffix(String name, String description, Category category)
	{
		super(name, description, category);
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event)
	{
		for (String s : Arrays.asList("/", ".", "-", ",", ":", ";", "'", "\"", "+", "\\"))
		{
			if (event.getMessage().startsWith(s)) return;
		}

		event.setMessage(event.getMessage() + " ｜ Serenity");
	}
}