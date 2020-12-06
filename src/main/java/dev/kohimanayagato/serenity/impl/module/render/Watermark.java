package dev.kohimanayagato.serenity.impl.module.render;


import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.font.FontUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Arrays;

public class Watermark extends Module
{
    private final Setting Position = new Setting("Mode", this, Arrays.asList(
            "Right Top",
            "Right Bottom",
            "Left Top",
            "Left Bottom"
    ));
    public Watermark(String name, String description, Category category)
    {
        super(name, description, category);
    }



    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event)
    {
        ScaledResolution screen = new ScaledResolution(mc);
        if (nullCheck() || !event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) return;

        if(Position.getEnumValue().equalsIgnoreCase("Right Top")) {
            FontUtil.drawStringWithShadow("Serenity 0.6.1", screen.getScaledWidth_double() - FontUtil.getStringWidth("Serenity Alpha 0.6-DEV") - 4, 2.0D , new Color(255, 255, 255, 232).getRGB() ); }

        if(Position.getEnumValue().equalsIgnoreCase("Right Bottom")) {
            FontUtil.drawStringWithShadow("Serenity 0.6.1", screen.getScaledWidth_double() - FontUtil.getStringWidth("Serenity Alpha 0.6-DEV") - 4, screen.getScaledHeight_double() - FontUtil.getFontHeight() + 1 , new Color(255, 255, 255, 232).getRGB() ); }

        if(Position.getEnumValue().equalsIgnoreCase("Left Top")) {
            FontUtil.drawStringWithShadow("Serenity 0.6.1", 2.0D, 2.0D , new Color(255, 255, 255, 232).getRGB() ); }

        if(Position.getEnumValue().equalsIgnoreCase("Left Bottom")) {
            FontUtil.drawStringWithShadow("Serenity 0.6.1", 2.0D,  screen.getScaledHeight_double() - FontUtil.getFontHeight() - 2, new Color(255, 255, 255, 232).getRGB() ); }
    }

}
