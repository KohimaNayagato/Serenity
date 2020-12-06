package dev.kohimanayagato.serenity.impl.module.misc;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;
import dev.kohimanayagato.serenity.api.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class PacketMine extends Module
{
	private final Setting render = new Setting("Render", this, true);

	private int timer = -1;
	private BlockPos renderBlock = null;

	public PacketMine(String name, String description, Category category)
	{
		super(name, description, category);
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onBlockClick(PlayerInteractEvent.LeftClickBlock event)
	{
		if (nullCheck()) return;

		if (mc.world.getBlockState(event.getPos()).getBlock().getBlockHardness(mc.world.getBlockState(event.getPos()), mc.world, event.getPos()) != -1)
		{
			mc.player.swingArm(EnumHand.MAIN_HAND);
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), Objects.requireNonNull(event.getFace())));
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
			if (renderBlock == null && render.getBooleanValue()) renderBlock = event.getPos();
		}
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event)
	{
		if (nullCheck()) return;

		if (renderBlock != null && timer > 0)
		{
			RenderUtil.drawBoxFromBlockpos(renderBlock, 0.3f, 0.3f, 0.3f, 0.5f);
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if (nullCheck()) return;

		if (renderBlock != null && mc.world.getBlockState(renderBlock).getBlock() == Blocks.AIR) renderBlock = null;
		if (renderBlock != null && timer == -1) timer = 130;
		if (timer > 0) timer--;
		if (timer == 0)
		{
			timer = -1;
			renderBlock = null;
		}
	}
}