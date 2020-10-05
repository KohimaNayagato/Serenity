package dev.kohimanayagato.serenity.impl.module.movement;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;
import dev.kohimanayagato.serenity.api.util.PlayerUtil;
import dev.kohimanayagato.serenity.impl.event.MoveEvent;
import dev.kohimanayagato.serenity.impl.event.WalkEvent;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.IMinecraft;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.ITimer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class Speed extends Module
{
	private final Setting mode = new Setting.Builder(SettingType.ENUM)
			.setName("Mode")
			.setModule(this)
			.addEnumValue("TP")
			.addEnumValue("Strafe")
			.setEnumValue("Strafe")
			.build();

	private final Setting speed = new Setting.Builder(SettingType.INTEGER)
			.setName("Speed")
			.setModule(this)
			.setIntegerValue(9)
			.setMinIntegerValue(1)
			.setMaxIntegerValue(100)
			.build();

	private final Setting useTimer = new Setting.Builder(SettingType.BOOLEAN)
			.setName("UseTimer")
			.setModule(this)
			.setBooleanValue(false)
			.build();

	private final Setting timerSpeed = new Setting.Builder(SettingType.INTEGER)
			.setName("TimerSpeed")
			.setModule(this)
			.setIntegerValue(7)
			.setMinIntegerValue(1)
			.setMaxIntegerValue(20)
			.build();
	
	private int currentStage;
	private double currentSpeed;
	private double distance;
	private int cooldown;
	
	public Speed(String name, String description, Category category)
	{
		super(name, description, category);

		addSetting(mode);
		addSetting(speed);
		addSetting(useTimer);
		addSetting(timerSpeed);
	}

	public void onEnable()
	{
		currentSpeed = PlayerUtil.vanillaSpeed();
		
		if (!mc.player.onGround) currentStage = 3;
	}

	@Override
	public void onDisable()
	{
		currentSpeed = 0.0;
		currentStage = 2;

		((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if (mc.player == null || mc.world == null) return;

		if (useTimer.getBooleanValue()) ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / ((timerSpeed.getIntegerValue() + 100) / 100f));
		else ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f);
	}

	@SubscribeEvent
	public void onUpdateWalkingPlayer(WalkEvent event)
	{
		distance = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
	}

	@SubscribeEvent
	public void onMove(MoveEvent event)
	{
		if (mode.getEnumValue().equalsIgnoreCase("Strafe"))
		{
			float forward = mc.player.movementInput.moveForward;
			float strafe = mc.player.movementInput.moveStrafe;
			float yaw = mc.player.rotationYaw;

			if (currentStage == 1 && PlayerUtil.isMoving())
			{
				currentStage = 2;
				currentSpeed = 1.18f * PlayerUtil.vanillaSpeed() - 0.01;
			}
			else if (currentStage == 2)
			{
				currentStage = 3;

				if (PlayerUtil.isMoving())
				{
					event.setY(mc.player.motionY = 0.4);
					if (cooldown > 0) --cooldown;
					currentSpeed *= speed.getIntegerValue() / 5f;
				}
			}
			else if (currentStage == 3)
			{
				currentStage = 4;
				currentSpeed = distance - (0.66 * (distance - PlayerUtil.vanillaSpeed()));
			}
			else
			{
				if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically)
					currentStage = 1;
				currentSpeed = distance - distance / 159.0;
			}

			currentSpeed = Math.max(currentSpeed, PlayerUtil.vanillaSpeed());

			if (forward == 0.0f && strafe == 0.0f)
			{
				event.setX(0.0);
				event.setZ(0.0);

				currentSpeed = 0.0;
			}
			else if (forward != 0.0f)
			{
				if (strafe >= 1.0f)
				{
					yaw += ((forward > 0.0f) ? -45.0f : 45.0f);
					strafe = 0.0f;
				}
				else
				{
					if (strafe <= -1.0f)
					{
						yaw += ((forward > 0.0f) ? 45.0f : -45.0f);
						strafe = 0.0f;
					}
				}
				if (forward > 0.0f) forward = 1.0f;
				else if (forward < 0.0f) forward = -1.0f;
			}

			double motionX = Math.cos(Math.toRadians(yaw + 90.0f));
			double motionZ = Math.sin(Math.toRadians(yaw + 90.0f));

			if (cooldown == 0)
			{
				event.setX(forward * currentSpeed * motionX + strafe * currentSpeed * motionZ);
				event.setZ(forward * currentSpeed * motionZ - strafe * currentSpeed * motionX);
			}

			if (forward == 0.0f && strafe == 0.0f)
			{
				event.setX(0.0);
				event.setZ(0.0);
			}
		}
		else
		{
			if (PlayerUtil.isMoving() && mc.player.onGround)
			{
				for (double d = 0.0625; d < (speed.getIntegerValue() / 10f); d += 0.262)
				{
					float rotation = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

					if (mc.player.movementInput.moveForward != 0.0f)
					{
						if (mc.player.movementInput.moveStrafe > 0.0f) rotation += (float)(mc.player.movementInput.moveForward > 0.0f ? -45 : 45);
						else if (mc.player.movementInput.moveStrafe < 0.0f) rotation += (float)(mc.player.movementInput.moveForward > 0.0f ? 45 : -45);

						mc.player.movementInput.moveStrafe = 0.0f;

						if (mc.player.movementInput.moveForward > 0.0f) mc.player.movementInput.moveForward = 1.0f;
						else if (mc.player.movementInput.moveForward < 0.0f) mc.player.movementInput.moveForward = -1.0f;
					}

					double cos = Math.cos(Math.toRadians(rotation + 90.0f));
					double sin = Math.sin(Math.toRadians(rotation + 90.0f));
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + (mc.player.movementInput.moveForward * d * cos + (double)mc.player.movementInput.moveStrafe * d * sin), mc.player.posY, mc.player.posZ + (mc.player.movementInput.moveForward * d * sin - (double)mc.player.movementInput.moveStrafe * d * cos), mc.player.onGround));
				}
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
			}
		}
	}
}