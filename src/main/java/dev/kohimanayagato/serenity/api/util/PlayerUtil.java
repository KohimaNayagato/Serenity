package dev.kohimanayagato.serenity.api.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;


public class PlayerUtil
{
	private static final Minecraft mc = Minecraft.getMinecraft();

	public static double vanillaSpeed()
	{
		double baseSpeed = 0.272;
		if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.SPEED))
		{
			final int amplifier = Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * amplifier;
		}
		return baseSpeed;
	}

	public static boolean isMoving()
	{
		return Minecraft.getMinecraft().player.moveForward != 0.0 || Minecraft.getMinecraft().player.moveStrafing != 0.0;
	}

	public static int getSlot(Item item)
	{
		for (int i = 0; i < 9; i++)
		{
			Item item1 = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();

			if (item.equals(item1))
			{
				return i;
			}
		}
		return -1;
	}

	public static int getSlot(Block block)
	{
		for (int i = 0; i < 9; i++)
		{
			Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();

			if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block))
			{
				return i;
			}
		}
		return -1;
	}

	public static void placeBlock(BlockPos pos)
	{
		for (EnumFacing enumFacing : EnumFacing.values())
		{
			if (!mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos))
			{
				Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getXOffset() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getYOffset() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getZOffset() * 0.5D);

				float[] old = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};

				mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - mc.player.posZ), (vec.x - mc.player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (mc.player.posY + (double) mc.player.getEyeHeight())), (Math.sqrt((vec.x - mc.player.posX) * (vec.x - mc.player.posX) + (vec.z - mc.player.posZ) * (vec.z - mc.player.posZ)))))), mc.player.onGround));
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
				mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
				mc.player.swingArm(EnumHand.MAIN_HAND);
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], mc.player.onGround));

				return;
			}
		}
	}

	public static boolean isIntercepted(BlockPos pos)
	{
		for (Entity entity : mc.world.loadedEntityList)
		{
			if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
		}

		return false;
	}

	public static double getDirection()
	{
		float rotationYaw = mc.player.rotationYaw;

		if (mc.player.moveForward < 0f) rotationYaw += 180f;

		float forward = 1f;

		if (mc.player.moveForward < 0f) forward = -0.5f;
		else if (mc.player.moveForward > 0f) forward = 0.5f;

		if (mc.player.moveStrafing > 0f) rotationYaw -= 90f * forward;
		if (mc.player.moveStrafing < 0f) rotationYaw += 90f * forward;

		return Math.toRadians(rotationYaw);
	}
}