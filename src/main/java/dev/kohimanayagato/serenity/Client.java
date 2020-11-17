package dev.kohimanayagato.serenity;

import dev.kohimanayagato.serenity.api.EventHandler;
import dev.kohimanayagato.serenity.api.command.CommandManager;
import dev.kohimanayagato.serenity.api.gui.clickgui.ClickGUI;
import dev.kohimanayagato.serenity.api.manager.FriendManager;
import dev.kohimanayagato.serenity.api.module.ModuleManager;
import dev.kohimanayagato.serenity.api.setting.SettingManager;
import dev.kohimanayagato.serenity.api.util.font.CustomFontRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import dev.kohimanayagato.serenity.api.manager.ConfigManager;

import java.awt.*;
import java.io.IOException;

@Mod(modid = "serenity", name = "Serenity", version = "0.4")
public class Client
{
	public static ModuleManager moduleManager;
	public static SettingManager settingManager;
	public static CustomFontRenderer customFontRenderer;
	public static ClickGUI clickGUI;
	public static CommandManager commandManager;
	public static FriendManager friendManager;

	@Mod.EventHandler
	public void initialize(FMLInitializationEvent event) throws IOException {
		commandManager = new CommandManager();
		settingManager = new SettingManager();
		moduleManager = new ModuleManager();
		friendManager = new FriendManager();
		customFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 19), true, false);
		clickGUI = new ClickGUI();

		ConfigManager.loadConfig();

		Runtime.getRuntime().addShutdownHook(new ConfigManager());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	public static ModuleManager getModuleManager()
	{
		return moduleManager;
	}
	public static FriendManager getFriendManager()
	{
		return friendManager;
	}
}
