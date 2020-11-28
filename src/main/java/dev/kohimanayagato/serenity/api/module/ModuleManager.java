package dev.kohimanayagato.serenity.api.module;

import dev.kohimanayagato.serenity.impl.module.combat.*;
import dev.kohimanayagato.serenity.impl.module.exploit.Blink;
import dev.kohimanayagato.serenity.impl.module.exploit.PacketMine;
import dev.kohimanayagato.serenity.impl.module.chat.ChatSuffix;
import dev.kohimanayagato.serenity.impl.module.chat.Shrug;
import dev.kohimanayagato.serenity.impl.module.exploit.Timer;
import dev.kohimanayagato.serenity.impl.module.misc.RPC;
import dev.kohimanayagato.serenity.impl.module.movement.LongJump;
import dev.kohimanayagato.serenity.impl.module.movement.Speed;
import dev.kohimanayagato.serenity.impl.module.movement.Sprint;
import dev.kohimanayagato.serenity.impl.module.movement.AutoHole;
import dev.kohimanayagato.serenity.impl.module.render.ClickGUI;
import dev.kohimanayagato.serenity.impl.module.render.CustomFont;
//import dev.kohimanayagato.serenity.impl.module.render.HoleESP;
import dev.kohimanayagato.serenity.impl.module.component.Watermark;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleManager
{
	private final ArrayList<Module> modules = new ArrayList<>();

	public ModuleManager()
	{
		// Hidden Category
		modules.add(new ClickGUI("ClickGUI", "Toggle modules by clicking on them", Category.HIDDEN));

		// Render Category
		modules.add(new CustomFont("CustomFont", "Use a custom font render instead of Minecraft's default", Category.RENDER));
		//modules.add(new HoleESP( "HoleEsp", "Renders nearby holes", Category.RENDER));

		// Exploit Category
		modules.add(new PacketMine("PacketMine", "Mine blocks with packets", Category.EXPLOIT));
		modules.add(new Timer("Timer", "Speeds up your game", Category.EXPLOIT));
		modules.add(new Blink("Blink", "Fake lag", Category.EXPLOIT));

		// Combat Category
		modules.add(new Criticals("Criticals", "Deal critical hits without jumping", Category.COMBAT));
		modules.add(new AutoCrystal("AutoCrystal", "Automatically attack other players with Crystals", Category.COMBAT));
		modules.add(new Surround("Surround", "Places blocks around you", Category.COMBAT));
		modules.add(new AutoTrap("AutoTrap", "Traps players", Category.COMBAT));
		modules.add(new AutoLog("AutoLog", "Automatically logs out when your health is low", Category.COMBAT));
		modules.add(new Auto32K("Auto32K", "Automatically handles 32ks for you in combat", Category.COMBAT));

		// Movement Category
		modules.add(new Speed("Speed", "Allows you to move faster", Category.MOVEMENT));
		modules.add(new Sprint("Sprint", "Automatically toggles sprint for you", Category.MOVEMENT));
		modules.add(new AutoHole("AutoHole", "Automatically goes into holes for you", Category.MOVEMENT));
		modules.add(new LongJump("LongJump", "Jumps far", Category.MOVEMENT));

		// Chat Category
		modules.add(new ChatSuffix("ChatSuffix", "Adds a suffix to your chat messages", Category.CHAT));
		modules.add(new Shrug("Shrug", "Adds the shrug emoji when used", Category.CHAT));

		// Misc Category
		modules.add(new RPC("DiscordRPC", "Shares your game status in Discord", Category.MISC));

		//Component Category
		modules.add(new Watermark ("Watermark", "Puts a watermark of serenity in the corner of your screen", Category.COMPONENT));
		//modules.add(new ArrayList("ArrayList", "Displays enabled modules", Category.COMPONENT));

	}

	public ArrayList<Module> getModules()
	{
		return modules;
	}

	public Module getModule(String name)
	{
		for (Module module : modules)
		{
			if (module.getName().equalsIgnoreCase(name)) return module;
		}

		return null;
	}

	public ArrayList<Module> getModules(Category category)
	{
		ArrayList<Module> mods = new ArrayList<>();

		for (Module module : modules)
		{
			if (module.getCategory().equals(category)) mods.add(module);
		}

		return mods;
	}

	public ArrayList<Module> getEnabledModules()
	{
		return modules.stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::new));
	}
}
