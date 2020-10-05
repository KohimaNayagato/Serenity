package dev.kohimanayagato.serenity.mixin.mixins.mixin;

import dev.kohimanayagato.serenity.mixin.mixins.accessor.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Minecraft.class, priority =  634756347)
public class MinecraftMixin implements IMinecraft
{
	@Final @Shadow private Timer timer;

	@Override
	public Timer getTimer()
	{
		return timer;
	}
}
