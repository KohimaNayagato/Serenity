package dev.kohimanayagato.serenity.impl.module.render;

import dev.kohimanayagato.serenity.api.module.ModuleManager;
import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.util.LoggerUtil;
import dev.kohimanayagato.serenity.api.util.font.FontUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.gui.ScaledResolution;

import java.util.logging.Logger;

public class ArrayList extends Module {

    public ArrayList(String name, String description, Category category)
    {
        super(name, description, category);
    }


    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event){

        }
    }
