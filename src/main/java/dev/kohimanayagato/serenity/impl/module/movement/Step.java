package dev.kohimanayagato.serenity.impl.module.movement;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.IMinecraft;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.ITimer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Step extends Module {

    public Setting jumpSpeed = new Setting("JumpSpeed", this, 20, 1, 600);

    public Step(String name, String description, Category category) {
        super(name, description, category);
    }

    boolean resetTimer = false;
    // Direction playerMovingDirection = Direction.NORTH;

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (mc.player == null)
            return;

        if (mc.player.collidedHorizontally && mc.player.onGround && !checkCollisionAtHeadWhereMoving()) {
            mc.player.motionY = 0.42f;
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / (jumpSpeed.getIntegerValue() / 10f));
            resetTimer = true;
        } else if (resetTimer && !mc.player.collidedHorizontally) {
            ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f);
            resetTimer = false;
        }
    }

    private boolean checkCollisionAtHeadWhereMoving() {
        // TODO do this based off movement rather than where looking
        BlockPos headPos = mc.player.getPosition().add(0, 1, 0);
        float yaw = (mc.player.rotationYaw % 360 + 360) % 360;
        if (yaw > 135 || yaw < -135) {
            return (mc.world.getBlockState(headPos.north()).getMaterial().isSolid());
        } else if (yaw < -45) {
            return (mc.world.getBlockState(headPos.east()).getMaterial().isSolid());
        } else if (yaw > 45) {
            return (mc.world.getBlockState(headPos.south()).getMaterial().isSolid());
        } else {
            return (mc.world.getBlockState(headPos.west()).getMaterial().isSolid());
        }
        /*
        switch (playerMovingDirection) {
            case NORTH:
                return (mc.world.getBlockState(headPos.north()).getMaterial().isSolid());
            case SOUTH:
                return (mc.world.getBlockState(headPos.south()).getMaterial().isSolid());
            case EAST:
                return (mc.world.getBlockState(headPos.east()).getMaterial().isSolid());
            case WEST:
                return (mc.world.getBlockState(headPos.west()).getMaterial().isSolid());
        }

        return true;

         */
    }

    /*
    @SubscribeEvent
    public void onPlayerMove(MoveEvent event) {

        if (event.getX() > 0 && event.getZ() > 0) {
            if (event.getX() > event.getZ()) {
                playerMovingDirection = Direction.EAST;
            } else {
                playerMovingDirection = Direction.NORTH;
            }
        } else if (event.getX() < 0 && event.getZ() > 0) {
            if (Math.abs(event.getX()) > event.getZ()) {
                playerMovingDirection = Direction.WEST;
            } else {
                playerMovingDirection = Direction.NORTH;
            }
        } else if (event.getX() < 0 && event.getZ() < 0) {
            if (Math.abs(event.getX()) > Math.abs(event.getZ())) {
                playerMovingDirection = Direction.WEST;
            } else {
                playerMovingDirection = Direction.SOUTH;
            }
        } else {
            if (event.getX() > Math.abs(event.getZ())) {
                playerMovingDirection = Direction.EAST;
            } else {
                playerMovingDirection = Direction.SOUTH;
            }
        }

        /
        BlockPos movedVector = mc.player.getPosition().subtract(lastPlayerBlockPos);
        if (movedVector.getX() > 0 && movedVector.getZ() > 0) {
            if (movedVector.getX() > movedVector.getZ()) {
                playerMovingDirection = Direction.EAST;
            } else {
                playerMovingDirection = Direction.NORTH;
            }
        } else if (movedVector.getX() < 0 && movedVector.getZ() > 0) {
            if (Math.abs(movedVector.getX()) > movedVector.getZ()) {
                playerMovingDirection = Direction.WEST;
            } else {
                playerMovingDirection = Direction.NORTH;
            }
        } else if (movedVector.getX() < 0 && movedVector.getZ() < 0) {
            if (Math.abs(movedVector.getX()) > Math.abs(movedVector.getZ())) {
                playerMovingDirection = Direction.WEST;
            } else {
                playerMovingDirection = Direction.SOUTH;
            }
        } else {
            if (movedVector.getX() > Math.abs(movedVector.getZ())) {
                playerMovingDirection = Direction.EAST;
            } else {
                playerMovingDirection = Direction.SOUTH;
            }
        }
        lastPlayerBlockPos = mc.player.getPosition();

         /

    }
    */

    /*
    enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

     */

}
