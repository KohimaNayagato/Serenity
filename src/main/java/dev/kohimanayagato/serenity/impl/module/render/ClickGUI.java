package dev.kohimanayagato.serenity.impl.module.render;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;


public class ClickGUI extends Module
{

	Setting color = new Setting("Color", this, Arrays.asList(
			"Purple"
	));

	public ClickGUI(String name, String description, Category category)
	{
		super(name, description, category);

		setBind(Keyboard.KEY_P);
	}

	@Override
	public void onEnable()
	{
		mc.displayGuiScreen(Client.clickGUI);
	}
}