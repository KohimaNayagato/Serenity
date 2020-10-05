package dev.kohimanayagato.serenity.mixin.mixins.mixin;

import dev.kohimanayagato.serenity.mixin.mixins.accessor.ITimer;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Timer.class, priority = 634756347)
public class TimerMixin implements ITimer
{
	@Shadow private float tickLength;

	@Override
	public float getTickLength()
	{
		return tickLength;
	}

	@Override
	public void setTickLength(float tickLength)
	{
		this.tickLength = tickLength;
	}
}
