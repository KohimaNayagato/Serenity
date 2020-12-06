package dev.kohimanayagato.serenity.impl.module.combat;

import dev.kohimanayagato.serenity.Client;
import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.RenderUtil;
import dev.kohimanayagato.serenity.impl.event.PacketEvent;
import dev.kohimanayagato.serenity.mixin.mixins.accessor.ICPacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class AutoCrystal extends Module
{
    private final Setting attackSpeed = new Setting("AttackSpeed", this, 17, 0, 20);
    private final Setting placeSpeed = new Setting("PlaceSpeed", this, 18, 0, 20);
    private final Setting placeRange = new Setting("PlaceRange", this, 5, 1, 10);
    private final Setting attackRange = new Setting("AttackRange", this, 4, 1, 10);
    private final Setting minDamage = new Setting("MinDamage", this, 4, 0, 16);
    private final Setting enemyRange = new Setting("EnemyRange", this, 9, 1, 20);
    private final Setting multiPlace = new Setting("MultiPlace", this, false);
    private final Setting onlyOwn = new Setting("OnlyOwn", this, true);
    private final Setting facePlaceHealth = new Setting("FacePlaceHealth", this, 7, 0, 36);
    private final Setting itemSwitch = new Setting("ItemSwitch", this, true);
    private final Setting color = new Setting("Color", this, Arrays.asList(
            "Static",
            "Rainbow"
    ));
    private final Setting red = new Setting("Red", this, 255, 0, 255);
    private final Setting green = new Setting("Green", this, 20, 0, 255);
    private final Setting blue = new Setting("Blue", this, 20, 0, 255);
    private final Setting alpha = new Setting("Alpha", this, 100, 0, 255);
    private final Setting rainbowSpeed = new Setting("RainbowSpeed", this, 5, 0, 10);

    private final ArrayList<BlockPos> ownCrystals = new ArrayList<>();
    private BlockPos render;
    private long placeSystemTime = -1L;
    private long breakSystemTime = -1L;
    private long multiPlaceSystemTime = -1L;
    private long antiStuckSystemTime = -1L;
    private boolean togglePitch = false;
    private boolean switchCooldown = false;
    private boolean isSpoofingAngles;
    private double yaw;
    private double pitch;
    private float hue = 0;

    public AutoCrystal(String name, String description, Category category)
    {
        super(name, description, category);
    }

    public float calculateDamage(double posX, double posY, double posZ, Entity entity)
    {
        double size = entity.getDistance(posX, posY, posZ) / 12.0D;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - size) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D));
        double finals = 1.0D;
        if (entity instanceof EntityLivingBase)
        {
            finals = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, mc.player, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finals;
    }

    public float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion)
    {
        float d = damage;
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            d = CombatRules.getDamageAfterAbsorb(d, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp((float) k, 0.0F, 20.0F);
            d *= 1.0F - f / 25.0F;
            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11))))
            {
                d -= d / 4.0F;
            }

        }
        else
        {
            d = CombatRules.getDamageAfterAbsorb(d, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }
        return d;
    }

    private float getDamageMultiplied(float damage)
    {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    public boolean canBlockBeSeen(BlockPos blockPos)
    {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), false, true, false) == null;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event)
    {
        if (mc.player == null || mc.world == null) return;

        if (event.getPacket() instanceof CPacketPlayer && isSpoofingAngles)
        {
            ((ICPacketPlayer) event.getPacket()).setYaw((float) yaw);
            ((ICPacketPlayer) event.getPacket()).setPitch((float) pitch);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter((entity) -> entity instanceof EntityEnderCrystal).min(Comparator.comparing((c) -> mc.player.getDistance(c))).orElse(null);

        if (crystal != null && mc.player.getDistance(crystal) <= (float) (Integer) attackRange.getIntegerValue())
        {
            if (System.nanoTime() / 1000000L - breakSystemTime >= (long) (420 - attackSpeed.getIntegerValue() * 20))
            {
                if (!onlyOwn.getBooleanValue() || ownCrystals.contains(render))
                {
                    lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
                    mc.playerController.attackEntity(mc.player, crystal);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    ownCrystals.remove(render);
                    breakSystemTime = System.nanoTime() / 1000000L;
                }
            }

            if (multiPlace.getBooleanValue())
            {
                if (System.nanoTime() / 1000000L - multiPlaceSystemTime >= (long) (20 * (placeSpeed.getIntegerValue())) && System.nanoTime() / 1000000L - antiStuckSystemTime <= (long) (400 + (400 - attackSpeed.getIntegerValue() * 20)))
                {
                    multiPlaceSystemTime = System.nanoTime() / 1000000L;
                    return;
                }
            }
            else if (System.nanoTime() / 1000000L - antiStuckSystemTime <= (long) (400 + (400 - attackSpeed.getIntegerValue() * 20)))
            {
                return;
            }
        }
        else
        {
            resetRotation();
        }

        int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1)
        {
            for (int l = 0; l < 9; ++l)
            {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL)
                {
                    crystalSlot = l;
                    break;
                }
            }
        }

        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
        {
            offhand = true;
        }
        else if (crystalSlot == -1)
        {
            return;
        }

        BlockPos finalPos = null;
        List<BlockPos> blocks = findCrystalBlocks();
        ArrayList<Entity> entities = mc.world.playerEntities.stream().filter((entityPlayer) -> !Client.friendManager.isFriend(entityPlayer.getName())).collect(Collectors.toCollection(ArrayList::new));
        double damage = 0.5D;
        Iterator<Entity> var11 = entities.iterator();

        label166:
        while (true)
        {
            Entity entity2;
            do
            {
                do
                {
                    do
                    {
                        if (!var11.hasNext())
                        {
                            if (damage == 0.5D)
                            {
                                render = null;
                                resetRotation();
                                return;
                            }

                            render = finalPos;
                            if (!offhand && mc.player.inventory.currentItem != crystalSlot)
                            {
                                if (itemSwitch.getBooleanValue())
                                {
                                    mc.player.inventory.currentItem = crystalSlot;
                                    resetRotation();
                                    switchCooldown = true;
                                }

                                return;
                            }

                            lookAtPacket((double) finalPos.getX() + 0.5D, (double) finalPos.getY() - 0.5D, (double) finalPos.getZ() + 0.5D, mc.player);
                            RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double) finalPos.getX() + 0.5D, (double) finalPos.getY() - 0.5D, (double) finalPos.getZ() + 0.5D));
                            EnumFacing f;
                            if (result != null && result.sideHit != null)
                            {
                                f = result.sideHit;
                            }
                            else
                            {
                                f = EnumFacing.UP;
                            }

                            if (switchCooldown)
                            {
                                switchCooldown = false;
                                return;
                            }

                            if (System.nanoTime() / 1000000L - placeSystemTime >= (long) (placeSpeed.getIntegerValue() * 5))
                            {
                                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(finalPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                                ownCrystals.add(finalPos);
                                antiStuckSystemTime = System.nanoTime() / 1000000L;
                                placeSystemTime = System.nanoTime() / 1000000L;
                            }

                            if (isSpoofingAngles)
                            {
                                EntityPlayerSP player;
                                if (togglePitch)
                                {
                                    player = mc.player;
                                    player.rotationPitch += 4.0E-4F;
                                    togglePitch = false;
                                }
                                else
                                {
                                    player = mc.player;
                                    player.rotationPitch -= 4.0E-4F;
                                    togglePitch = true;
                                }
                            }

                            return;
                        }

                        entity2 = var11.next();
                    }
                    while (entity2 == mc.player);
                }
                while (((EntityLivingBase) entity2).getHealth() <= 0.0F);
            }
            while (mc.player.getDistanceSq(entity2) > (double) ((Integer) enemyRange.getIntegerValue() * (Integer) enemyRange.getIntegerValue()));

            Iterator<BlockPos> var13 = blocks.iterator();

            while (true)
            {
                BlockPos blockPos;
                double d;
                double self;
                do
                {
                    do
                    {
                        do
                        {
                            double b;
                            do
                            {
                                do
                                {
                                    if (!var13.hasNext())
                                    {
                                        continue label166;
                                    }

                                    blockPos = var13.next();
                                }
                                while (!canBlockBeSeen(blockPos) && mc.player.getDistanceSq(blockPos) > 25.0D);

                                b = entity2.getDistanceSq(blockPos);
                            }
                            while (b > 56.2D);

                            d = calculateDamage((double) blockPos.getX() + 0.5D, blockPos.getY() + 1, (double) blockPos.getZ() + 0.5D, entity2);
                        }
                        while (d < (double) (Integer) minDamage.getIntegerValue() && ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount() > (float) (Integer) facePlaceHealth.getIntegerValue());
                    }
                    while (d <= damage);

                    self = calculateDamage((double) blockPos.getX() + 0.5D, blockPos.getY() + 1, (double) blockPos.getZ() + 0.5D, mc.player);
                }
                while (((double) (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self <= 7.0D || self > d));

                damage = d;
                finalPos = blockPos;
            }
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        if (render != null)
        {
            hue += rainbowSpeed.getIntegerValue() / 1000f;
            int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);

            int r = rgb >> 16 & 255;
            int g = rgb >> 8 & 255;
            int b = rgb & 255;

            if (color.getEnumValue().equals("Rainbow"))
            {
                RenderUtil.drawBoxFromBlockpos(render, r / 255f, g / 255f, b / 255f, alpha.getIntegerValue() / 255f);
            }
            else
            {
                RenderUtil.drawBoxFromBlockpos(render, red.getIntegerValue() / 255f, green.getIntegerValue() / 255f, blue.getIntegerValue() / 255f, alpha.getIntegerValue() / 255f);
            }
        }

    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me)
    {
        double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    private double[] calculateLookAt(double px, double py, double pz, EntityPlayer me)
    {
        double dirX = me.posX - px;
        double dirY = me.posY - py;
        double dirZ = me.posZ - pz;
        double len = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX /= len;
        dirY /= len;
        dirZ /= len;
        double pitch = Math.asin(dirY);
        double yaw = Math.atan2(dirZ, dirX);
        pitch = pitch * 180.0D / 3.141592653589793D;
        yaw = yaw * 180.0D / 3.141592653589793D;
        yaw += 90.0D;
        return new double[]{yaw, pitch};
    }

    public BlockPos getPlayerPos()
    {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private List<BlockPos> findCrystalBlocks()
    {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(getPlayerPos(), ((Integer) placeRange.getIntegerValue()).floatValue()).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    private boolean canPlaceCrystal(Object o)
    {
        BlockPos blockPos = (BlockPos) o;

        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    private List<BlockPos> getSphere(BlockPos loc, float r)
    {
        List<BlockPos> blocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x)
        {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z)
            {
                for (int y = cy - (int) r; (float) y < ((float) cy + r); ++y)
                {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + ((cy - y) * (cy - y));
                    if (dist < (double) (r * r))
                    {
                        BlockPos l = new BlockPos(x, y, z);
                        blocks.add(l);
                    }
                }
            }
        }

        return blocks;
    }

    private void setYawAndPitch(float yaw1, float pitch1)
    {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private void resetRotation()
    {
        if (isSpoofingAngles)
        {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }



    @Override
    public void onDisable()
    {
        render = null;
        resetRotation();
    }
}