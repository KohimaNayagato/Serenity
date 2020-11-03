
package dev.kohimanayagato.serenity.impl.module.chat;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;

import java.util.ArrayList;
import java.util.Arrays;

//MADE BY LNADAV SOMETIME IN NOVEMBER 2020

public class BetterChat extends Module
{
	private final Setting Mode = new Setting("Mode", this, Arrays.asList(
		//"Append",
		"Replace"
		));
		
	public BetterChat(String name, String description, Category category)
	{
		super(name, description, category);
		addSetting(Mode);
		
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event)
	{
		/*
		if ( Mode.getEnumValue().equalsIgnoreCase("Append") ){
			if ( event.getMessage().contains("shrug")) {
				event.setMessage(event.getMessage().replace("shrug", "") + " \u00AF\\_(\u30C4)_/\u00AF");
			}
			/*
			if ( event.getMessage().contains("lenny")) {
				event.setMessage(event.getMessage().replace("lenny", "") + " ( ͡° ͜ʖ ͡°)");
			}
			
		}
		*/
		if ( Mode.getEnumValue().equalsIgnoreCase("Replace") ){
			if ( event.getMessage().contains("shrug") ) {
				event.setMessage(event.getMessage().replace("shrug", "\u00AF\\_(\u30C4)_/\u00AF"));
			}
			/*
			if ( event.getMessage().contains("lenny") ) {
				event.setMessage(event.getMessage().replace("lenny", " ( ͡° ͜ʖ ͡°) "));
			}
			*/
		}
	}
}