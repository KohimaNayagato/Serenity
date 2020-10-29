package dev.kohimanayagato.serenity.api.module;

import dev.kohimanayagato.serenity.impl.module.combat.AutoLog;
import dev.kohimanayagato.serenity.impl.module.combat.AutoTrap;
import dev.kohimanayagato.serenity.impl.module.combat.Criticals;
import dev.kohimanayagato.serenity.impl.module.combat.OffHand;
import dev.kohimanayagato.serenity.impl.module.combat.Surround;
import dev.kohimanayagato.serenity.impl.module.exploit.Blink;
import dev.kohimanayagato.serenity.impl.module.exploit.PacketMine;
<<<<<<< Updated upstream
import dev.kohimanayagato.serenity.impl.module.misc.ChatSuffix;
import dev.kohimanayagato.serenity.impl.module.Chat.BetterChat;
import dev.kohimanayagato.serenity.impl.module.misc.Timer;
=======
import dev.kohimanayagato.serenity.impl.module.chat.BetterChat;
import dev.kohimanayagato.serenity.impl.module.chat.ChatSuffix;
import dev.kohimanayagato.serenity.impl.module.exploit.Timer;
>>>>>>> Stashed changes
import dev.kohimanayagato.serenity.impl.module.movement.LongJump;
import dev.kohimanayagato.serenity.impl.module.movement.Speed;
import dev.kohimanayagato.serenity.impl.module.render.ClickGUI;
import dev.kohimanayagato.serenity.impl.module.render.CustomFont;

import java.util.ArrayList;

public class ModuleManager
{
	private final ArrayList<Module> modules = new ArrayList<>();

	public ModuleManager()
	{
		modules.add(new ClickGUI("ClickGUI", "Toggle modules by clicking on them", Category.HIDDEN));
		modules.add(new CustomFont("CustomFont", "Use a custom font render instead of Minecraft's default", Category.RENDER));
		modules.add(new PacketMine("PacketMine", "Mine blocks with packets", Category.EXPLOIT));
		modules.add(new Timer("Timer", "Speeds up your game", Category.MISC));
		modules.add(new Criticals("Criticals", "Deal critical hits without jumping", Category.COMBAT));
		modules.add(new OffHand("OffHand", "Offhand shit idek", Category.COMBAT));
		modules.add(new LongJump("LongJump", "Jumps far", Category.MOVEMENT));
<<<<<<< Updated upstream
		modules.add(new ChatSuffix("ChatSuffix", "Adds a suffix to your chat messages", Category.MISC));
		modules.add(new BetterChat ("BetterChat", "Adds shrug and lennys to your message if you type it", Category.CHAT));
=======
		modules.add(new ChatSuffix("ChatSuffix", "Adds a suffix to your chat messages", Category.CHAT));
		modules.add(new BetterChat("BetterChat", "Adds shrug and lenny", Category.CHAT));
>>>>>>> Stashed changes
		modules.add(new Speed("Speed", "Allows you to move faster", Category.MOVEMENT));
		modules.add(new AutoLog("Sprint", "Automatically toggles sprint for you", Category.MOVEMENT));
		modules.add(new AutoLog("Anchor", "Automatically goes into holes for you", Category.MOVEMENT));
		modules.add(new Surround("Surround", "Places blocks around you", Category.COMBAT));
		modules.add(new AutoTrap("AutoTrap", "Traps players", Category.COMBAT));
		modules.add(new Blink("Blink", "Fake lag", Category.EXPLOIT));
		modules.add(new AutoLog("AutoLog", "Automatically logs out when your health is low", Category.COMBAT));
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
}
