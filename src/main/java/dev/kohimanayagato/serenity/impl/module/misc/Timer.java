package dev.kohimanayagato.serenity.impl.module.misc;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.IMinecraft;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.ITimer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Timer extends Module
{
	private final Setting speed = new Setting("Speed", this, 20, 1, 300);

	public Timer(String name, String description, Category category)
	{
		super(name, description, category);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / (speed.getIntegerValue() / 10f));
	}

	@Override
	public void onDisable()
	{
		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f);
	}
}