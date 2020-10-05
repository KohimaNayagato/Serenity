package dev.kohimanayagato.serenity.mixin.mixins.mixin;

import dev.kohimanayagato.serenity.impl.event.PacketReceiveEvent;
import dev.kohimanayagato.serenity.impl.event.PacketSendEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class, priority = 634756347)
public class NetworkManagerMixin
{
	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo)
	{
		PacketReceiveEvent event = new PacketReceiveEvent(packet);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) callbackInfo.cancel();
	}

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onPacketSend(Packet<?> packet, CallbackInfo callbackInfo)
	{
		PacketSendEvent event = new PacketSendEvent(packet);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) callbackInfo.cancel();
	}
}
