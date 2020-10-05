package dev.kohimanayagato.serenity.impl.module.combat;

import dev.kohimanayagato.serenity.api.module.Category;
import dev.kohimanayagato.serenity.api.module.Module;
import dev.kohimanayagato.serenity.api.setting.Setting;
import dev.kohimanayagato.serenity.api.setting.SettingType;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoLog extends Module
{
    private final Setting health = new Setting.Builder(SettingType.INTEGER)
            .setName("Health")
            .setModule(this)
            .setIntegerValue(10)
            .setMinIntegerValue(1)
            .setMaxIntegerValue(30)
            .build();

    public AutoLog(String name, String description, Category category)
    {
        super(name, description, category);

        addSetting(health);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.getHealth() <= health.getIntegerValue())
        {
            disable();
            mc.world.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
