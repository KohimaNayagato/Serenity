package dev.kohimanayagato.serenity.impl.module.combat;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;
import dev.kohimanayagato.serenity.api.util.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Surround extends Module
{
    private final Setting blocksPerTick = new Setting.Builder(SettingType.INTEGER)
            .setName("BPT")
            .setModule(this)
            .setIntegerValue(1)
            .setMinIntegerValue(1)
            .setMaxIntegerValue(10)
            .build();

    private final Setting disable = new Setting.Builder(SettingType.ENUM)
            .setName("Disable")
            .setModule(this)
            .setEnumValue("WhenDone")
            .addEnumValue("WhenDone")
            .addEnumValue("OnLeave")
            .addEnumValue("Off")
            .build();

    private final List<Vec3d> positions = new ArrayList<>(Arrays.asList(
            new Vec3d(1, -1, 0),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(0, -1, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(0, 0, -1)
        ));

    private boolean finished;

    public Surround(String name, String description, Category category)
    {
        super(name, description, category);

        addSetting(blocksPerTick);
        addSetting(disable);
    }

    @Override
    public void onEnable()
    {
        finished = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        if (finished && (disable.getEnumValue().equalsIgnoreCase("WhenDone") || (disable.getEnumValue().equalsIgnoreCase("OnLeave") && !mc.player.onGround))) disable();

        int blocksPlaced = 0;

        for (Vec3d position : positions)
        {
            BlockPos pos = new BlockPos(position.add(mc.player.getPositionVector()));

            if (mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
            {
                int oldSlot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = PlayerUtil.getSlot(Blocks.OBSIDIAN);
                PlayerUtil.placeBlock(pos);
                mc.player.inventory.currentItem = oldSlot;
                blocksPlaced++;

                if (blocksPlaced == blocksPerTick.getIntegerValue()) return;
            }
        }
        if (blocksPlaced == 0) finished = true;
    }
}
