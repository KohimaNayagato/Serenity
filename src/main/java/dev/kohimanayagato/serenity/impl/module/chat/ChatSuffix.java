package dev.kohimanayagato.serenity.impl.module.chat;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;

import java.util.Arrays;

public class ChatSuffix extends Module
{

	private final Setting useCommands = new Setting("Commands", this, true);

	public ChatSuffix(String name, String description, Category category)
	{
		super(name, description, category);
		addSetting(useCommands);
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event)
	{
		if ( !useCommands.getBooleanValue() ){
			for (String s : Arrays.asList("/", ".", "-", ",", ":", ";", "'", "\"", "+", "\\", "$", "*"))
			{
				if (event.getMessage().startsWith(s)) return;
			}
		}

		event.setMessage(event.getMessage() + " \uff5c Serenity");
	}
}