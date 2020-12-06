package dev.kohimanayagato.serenity.impl.module.render;


import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.RenderUtil;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockHighlight extends Module
{
    private final Setting color = new Setting("Color", this, new ArrayList<>(Arrays.asList("Static", "Rainbow")));
    private final Setting red = new Setting("Red", this, 255, 0, 255);
    private final Setting green = new Setting("Green", this, 20, 0, 255);
    private final Setting blue = new Setting("Blue", this, 20, 0, 255);
    private final Setting alpha = new Setting("Alpha", this, 100, 0, 255);
    private final Setting rainbowSpeed = new Setting("RainbowSpeed", this, 5, 0, 10);

    private float hue = 0f;

    public BlockHighlight(String name,String description , Category category)
    {
        super(name, description, category);
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event)
    {
        hue += rainbowSpeed.getIntegerValue() / 1000f;
        int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);

        int r = rgb >> 16 & 255;
        int g = rgb >> 8 & 255;
        int b = rgb & 255;

        RayTraceResult rayTraceResult = mc.objectMouseOver;

        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockPos = rayTraceResult.getBlockPos();

            if (mc.world.getBlockState(blockPos).getMaterial() != Material.AIR)
            {
                if (color.getEnumValue().equals("Rainbow"))
                {
                    RenderUtil.drawBoxFromBlockpos(blockPos, r / 255f, g / 255f, b / 255f, alpha.getIntegerValue() / 255f);
                }
                else
                {
                    RenderUtil.drawBoxFromBlockpos(blockPos, red.getIntegerValue() / 255f, green.getIntegerValue() / 255f, blue.getIntegerValue() / 255f, alpha.getIntegerValue() / 255f);
                }
            }
        }
    }
}