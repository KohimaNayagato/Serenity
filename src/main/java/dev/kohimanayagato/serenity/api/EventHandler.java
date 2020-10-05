package dev.kohimanayagato.serenity.api;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class EventHandler
{
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		if (!Keyboard.getEventKeyState()) return;

		for (Module module : Client.moduleManager.getModules())
		{
			if (module.getBind() == Keyboard.getEventKey())
			{
				module.toggle();
			}
		}
	}

	@SubscribeEvent
	public void onChatSend(ClientChatEvent event)
	{
		if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) return;

		if (event.getMessage().startsWith(Client.commandManager.getPrefix()))
		{
			event.setCanceled(true);
			Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
			Client.commandManager.runCommand(event.getMessage());
		}
	}

}
