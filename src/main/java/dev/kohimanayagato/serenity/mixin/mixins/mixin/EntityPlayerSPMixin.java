package dev.kohimanayagato.serenity.mixin.mixins.mixin;

import dev.kohimanayagato.serenity.impl.event.MoveEvent;
import dev.kohimanayagato.serenity.impl.event.WalkEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class, priority = 634756347)
public class EntityPlayerSPMixin extends AbstractClientPlayer
{
	public EntityPlayerSPMixin(World worldIn, GameProfile playerProfile)
	{
		super(worldIn, playerProfile);
	}

	@Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
	public void move(final AbstractClientPlayer player, final MoverType moverType, final double x, final double y, final double z)
	{
		MoveEvent event = new MoveEvent(moverType, x, y, z);
		MinecraftForge.EVENT_BUS.post(event);

		if (!event.isCanceled()) super.move(event.getType(), event.getX(), event.getY(), event.getZ());
	}

	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
	public void onUpdateWalkingPlayer(CallbackInfo ci)
	{
		WalkEvent event = new WalkEvent();
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) ci.cancel();
	}
}
