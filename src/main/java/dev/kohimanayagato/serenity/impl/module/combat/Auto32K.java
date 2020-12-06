package dev.kohimanayagato.serenity.impl.module.combat;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.impl.event.PacketEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Auto32K extends Module
{
    private final Setting mode = new Setting("Mode", this, Arrays.asList(
            "Dispenser",
            "Normal"
    ));
    private final Setting aura = new Setting("Aura", this, true);
    private final Setting noClose = new Setting("NoClose", this, false);
    private final Setting disable = new Setting("Disable", this, true);

    private final DecimalFormat df = new DecimalFormat("#.#");
    private int stage;
    private BlockPos placeTarget;
    private int obiSlot;
    private int dispenserSlot;
    private int shulkerSlot;
    private int redstoneSlot;
    private int hopperSlot;
    private boolean isSneaking;
    private int swordSlot;
    private final List<Block> shulkers = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    private final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
    private boolean shouldAura;

    public Auto32K(String name, String description, Category category)
    {
        super(name, description, category);
    }

    @Override
    public void onEnable()
    {
        if (mode.getEnumValue().equals("Normal")) onEnableNormal();
        else onEnableDispenser();
    }

    @Override
    public void onDisable()
    {
        shouldAura = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;
        if (shouldAura)
        {
            tickAura();
        }

        if (mode.getEnumValue().equals("Normal")) onTickNormal();
        else onTickDispenser();
    }

    @SubscribeEvent
    public void onClose(GuiScreenEvent.KeyboardInputEvent event)
    {
        if ((Keyboard.getEventKey() == Keyboard.KEY_ESCAPE || Keyboard.getEventKey() == mc.gameSettings.keyBindInventory.getKeyCode()) && disable.getBooleanValue())
        {
            disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event)
    {
        if (event.getPacket() instanceof CPacketCloseWindow && noClose.getBooleanValue())
        {
            event.setCanceled(true);
        }
    }

    private void tickAura()
    {
        if (mc.player.isDead)
        {
            return;
        }

        boolean shield = mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (mc.player.isHandActive() && !shield)
        {
            return;
        }

        for (Entity target : mc.world.loadedEntityList)
        {
            if (!(target instanceof EntityLivingBase))
            {
                continue;
            }
            if (target == mc.player)
            {
                continue;
            }
            if (mc.player.getDistance(target) > 5)
            {
                continue;
            }
            if (((EntityLivingBase) target).getHealth() <= 0)
            {
                continue;
            }
            if (!Client.friendManager.isFriend(target.getName()))
            {
                attack(target);
            }
        }
    }

    private void attack(Entity e)
    {
        if (checkSharpness(mc.player.getHeldItemMainhand()))
        {
            mc.playerController.attackEntity(mc.player, e);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    private void onTickDispenser()
    {

        if (mc.player == null || mc.world == null) return;


        if (stage == 0)
        {
            if (placeTarget == null) return;
            mc.player.inventory.currentItem = obiSlot;
            placeBlock(new BlockPos(placeTarget), EnumFacing.DOWN);

            mc.player.inventory.currentItem = dispenserSlot;
            placeBlock(new BlockPos(placeTarget.add(0, 1, 0)), EnumFacing.DOWN);

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.add(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

            stage = 1;
            return;

        }


        if (stage == 1)
        {
            if (!(mc.currentScreen instanceof GuiContainer))
            {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 1, shulkerSlot, ClickType.SWAP, mc.player);
            mc.player.closeScreen();

            mc.player.inventory.currentItem = redstoneSlot;
            placeBlock(new BlockPos(placeTarget.add(0, 2, 0)), EnumFacing.DOWN);

            stage = 2;
            return;
        }


        if (stage == 2)
        {
            Block block = mc.world.getBlockState(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()).up()).getBlock();
            if ((block instanceof BlockAir) || (block instanceof BlockLiquid))
            {
                return;
            }

            mc.player.inventory.currentItem = hopperSlot;
            placeBlock(new BlockPos(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite())), mc.player.getHorizontalFacing());

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

            mc.player.inventory.currentItem = shulkerSlot;

            stage = 3;
            return;

        }


        if (stage == 3)
        {

            if (!(mc.currentScreen instanceof GuiContainer))
            {
                return;
            }

            if (((GuiContainer) mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty())
            {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 0, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);

            if (aura.getBooleanValue())
            {
                shouldAura = true;
            }
            else
            {
                disable();
            }
        }
    }

    private void onTickNormal()
    {
        if (!(mc.currentScreen instanceof GuiContainer))
        {
            return;
        }

        if (swordSlot == -1)
        {
            return;
        }

        boolean swapReady = true;

        if (((GuiContainer) mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty())
        {
            swapReady = false;
        }

        if (!((GuiContainer) mc.currentScreen).inventorySlots.getSlot(swordSlot).getStack().isEmpty())
        {
            swapReady = false;
        }

        if (swapReady)
        {
            mc.playerController.windowClick(((GuiContainer) mc.currentScreen).inventorySlots.windowId, 0, swordSlot - 32, ClickType.SWAP, mc.player);
            if (aura.getBooleanValue())
            {
                shouldAura = true;
            }
            else
            {
                disable();
            }
        }
    }

    private void onEnableDispenser()
    {

        df.setRoundingMode(RoundingMode.CEILING);

        stage = 0;

        placeTarget = null;

        obiSlot = -1;
        dispenserSlot = -1;
        shulkerSlot = -1;
        redstoneSlot = -1;
        hopperSlot = -1;

        isSneaking = false;

        for (int i = 0; i < 9; i++)
        {

            if (obiSlot != -1 && dispenserSlot != -1 && shulkerSlot != -1 && redstoneSlot != -1 && hopperSlot != -1)
            {
                break;
            }

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock))
            {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block == Blocks.HOPPER)
            {
                hopperSlot = i;
            }
            else if (shulkers.contains(block))
            {
                shulkerSlot = i;
            }
            else if (block == Blocks.OBSIDIAN)
            {
                obiSlot = i;
            }
            else if (block == Blocks.DISPENSER)
            {
                dispenserSlot = i;
            }
            else if (block == Blocks.REDSTONE_BLOCK)
            {
                redstoneSlot = i;
            }

        }

        if (obiSlot == -1 || dispenserSlot == -1 || shulkerSlot == -1 || redstoneSlot == -1 || hopperSlot == -1)
        {
            disable();
            return;
        }


        if (mc.objectMouseOver == null)
        {
            disable();
            return;
        }

        placeTarget = mc.objectMouseOver.getBlockPos().up();
    }

    private void onEnableNormal()
    {

        df.setRoundingMode(RoundingMode.CEILING);

        int hopperSlot = -1;
        int shulkerSlot = -1;
        int obiSlot = -1;
        swordSlot = -1;

        for (int i = 0; i < 9; i++)
        {

            if (hopperSlot != -1 && shulkerSlot != -1 && obiSlot != -1)
            {
                break;
            }

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock))
            {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block == Blocks.HOPPER)
            {
                hopperSlot = i;
            }
            else if (shulkers.contains(block))
            {
                shulkerSlot = i;
            }
            else if (block == Blocks.OBSIDIAN)
            {
                obiSlot = i;
            }

        }

        if (hopperSlot == -1)
        {
            disable();
            return;
        }

        if (shulkerSlot == -1)
        {
            disable();
            return;
        }

        int range = (int) Math.ceil(5);

        List<BlockPos> placeTargetList = getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), range);
        Map<BlockPos, Double> placeTargetMap = new HashMap<>();

        BlockPos placeTarget = null;
        boolean useRangeSorting = false;

        for (BlockPos placeTargetTest : placeTargetList)
        {
            for (Entity entity : mc.world.loadedEntityList)
            {

                if (!(entity instanceof EntityPlayer))
                {
                    continue;
                }

                if (entity == mc.player)
                {
                    continue;
                }

                if (Client.friendManager.isFriend(entity.getName()))
                {
                    continue;
                }

                if (isAreaPlaceable(placeTargetTest))
                {
                    double distanceToEntity = entity.getDistance(placeTargetTest.getX(), placeTargetTest.getY(), placeTargetTest.getZ());

                    placeTargetMap.put(placeTargetTest, placeTargetMap.containsKey(placeTargetTest) ? placeTargetMap.get(placeTargetTest) + distanceToEntity : distanceToEntity);
                    useRangeSorting = true;
                }

            }
        }

        if (placeTargetMap.size() > 0)
        {

            placeTargetMap.forEach((k, v) ->
            {
                if (!isAreaPlaceable(k))
                {
                    placeTargetMap.remove(k);
                }
            });

            if (placeTargetMap.size() == 0)
            {
                useRangeSorting = false;
            }

        }

        if (useRangeSorting)
        {
            placeTarget = Collections.min(placeTargetMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        else
        {
            for (BlockPos pos : placeTargetList)
            {
                if (isAreaPlaceable(pos))
                {
                    placeTarget = pos;
                    break;
                }
            }

        }

        if (placeTarget == null)
        {
            disable();
            return;
        }

        mc.player.inventory.currentItem = hopperSlot;
        placeBlock(new BlockPos(placeTarget));

        mc.player.inventory.currentItem = shulkerSlot;
        placeBlock(new BlockPos(placeTarget.add(0, 1, 0)));


        if (isSneaking)
        {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

        mc.player.inventory.currentItem = shulkerSlot;
        BlockPos hopperPos = new BlockPos(placeTarget);
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(hopperPos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
        swordSlot = shulkerSlot + 32;
    }

    private void placeBlock(BlockPos pos, EnumFacing side)
    {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        if (!isSneaking)
        {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));

        faceVectorPacketInstant(hitVec);


        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private void faceVectorPacketInstant(Vec3d vec)
    {
        float[] rotations = getLegitRotations(vec);

        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
    }

    private float[] getLegitRotations(Vec3d vec)
    {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]{
                mc.player.rotationYaw
                        + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
                mc.player.rotationPitch + MathHelper
                        .wrapDegrees(pitch - mc.player.rotationPitch)
        };
    }

    private Vec3d getEyesPos()
    {
        return new Vec3d(mc.player.posX,
                mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ);
    }

    private List<BlockPos> getSphere(BlockPos loc, float r)
    {
        List<BlockPos> blocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++)
        {
            for (int z = cz - (int) r; z <= cz + r; z++)
            {
                for (int y = (cy - (int) r); y < (cy + r); y++)
                {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + ((cy - y) * (cy - y));
                    if (dist < r * r)
                    {
                        BlockPos l = new BlockPos(x, y, z);
                        blocks.add(l);
                    }
                }
            }
        }
        return blocks;
    }

    private boolean isAreaPlaceable(BlockPos blockPos)
    {

        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos)))
        {
            if (entity instanceof EntityLivingBase)
            {
                return false;
            }
        }

        if (!mc.world.getBlockState(blockPos).getMaterial().isReplaceable())
        {
            return false;
        }

        if (!mc.world.getBlockState(blockPos.add(0, 1, 0)).getMaterial().isReplaceable())
        {
            return false;
        }

        if (mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() instanceof BlockAir)
        {
            return false;
        }

        if (mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() instanceof BlockLiquid)
        {
            return false;
        }

        if (mc.player.getPositionVector().distanceTo(new Vec3d(blockPos)) > 5)
        {
            return false;
        }

        Block block = mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock();
        if (blackList.contains(block) || shulkers.contains(block))
        {
            return false;
        }

        return mc.player.getPositionVector().distanceTo(new Vec3d(blockPos).add(0, 1, 0)) <= 5;

    }

    private void placeBlock(BlockPos pos)
    {

        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable())
        {
            return;
        }

        if (!checkForNeighbours(pos))
        {
            return;
        }

        for (EnumFacing side : EnumFacing.values())
        {

            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();

            if (mc.world.getBlockState(neighbor).getBlock().canCollideCheck(mc.world.getBlockState(neighbor), false))
            {
                Vec3d hitVec = new Vec3d(neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));

                Block neighborPos = mc.world.getBlockState(neighbor).getBlock();
                if (blackList.contains(neighborPos) || shulkers.contains(neighborPos))
                {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    isSneaking = true;
                }

                faceVectorPacketInstant(hitVec);
                mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
//			mc.rightClickDelayTimer = 4;

                return;
            }

        }

    }

    private boolean checkForNeighbours(BlockPos blockPos)
    {
        if (!hasNeighbour(blockPos))
        {

            for (EnumFacing side : EnumFacing.values())
            {
                BlockPos neighbour = blockPos.offset(side);
                if (hasNeighbour(neighbour))
                {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean hasNeighbour(BlockPos blockPos)
    {
        for (EnumFacing side : EnumFacing.values())
        {
            BlockPos neighbour = blockPos.offset(side);
            if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable())
            {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("ALL")
    private boolean checkSharpness(ItemStack stack)
    {
        if (stack.getTagCompound() == null)
        {
            return false;
        }

        NBTTagList enchants = (NBTTagList) stack.getTagCompound().getTag("ench");

        if (enchants == null)
        {
            return false;
        }

        for (int i = 0; i < enchants.tagCount(); i++)
        {
            NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16)
            {
                int lvl = enchant.getInteger("lvl");
                if (lvl >= 42)
                {
                    return true;
                }
                break;
            }
        }
        return false;
    }
}